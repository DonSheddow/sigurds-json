import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.lexer.token
import com.github.h0tk3y.betterParse.parser.Parser
import com.github.h0tk3y.betterParse.utils.Tuple2
import java.awt.Color

fun indentExceptMagicString(s: String): String {
    val regex = Regex("<<<START_JSON_ENCODING>>>.*?<<<STOP_JSON_ENCODING>>>", RegexOption.DOT_MATCHES_ALL)
    val indented = s.replace("\n", "\n" + INDENT)
    return regex.replace(indented) {
        it.groupValues[0].replace("\n" + INDENT, "\n")
    }
}

class ColoredStrings(val strings: List<Pair<String, Color?>>) {
    operator fun plus(other: ColoredStrings): ColoredStrings {
        return ColoredStrings(this.strings + other.strings)
    }

    fun indent(): ColoredStrings {
        return ColoredStrings(strings.map {(a, b) -> Pair(indentExceptMagicString(a), b) } )
    }

    fun toPlainString(): String {
        return strings.joinToString("") { it.first }
    }
}

fun <T> joinCS(l: List<T>, separator: ColoredStrings, f: (T) -> ColoredStrings): ColoredStrings {
    if (l.isEmpty()) {
        return cs("")
    }
    val first = f(l[0])
    return l.subList(1, l.size).fold(first) { acc, x -> acc + separator + f(x) }
}
fun joinCS(l: List<ColoredStrings>, separator: ColoredStrings): ColoredStrings = joinCS(l, separator) { it }

fun cs(str: String, color: Color?): ColoredStrings = ColoredStrings(listOf(Pair(str, color)))
fun cs(str: String): ColoredStrings = cs(str, null)


class JsonGrammar<T>(private val valFunc: (String) -> T, private val arrayFunc: (List<T>) -> T, private val objectFunc: (List<Tuple2<String, T>>) -> T) : Grammar<T>() {
    private val stringLiteral by token { input, from ->
        if (input[from] != '"') {
            return@token 0
        }
        else {
            var idx = from + 1
            while (idx < input.length) {
                if (input[idx] == '"') {
                    return@token idx - from + 1
                }
                else if (input[idx] == '\\') {
                    idx += 2
                }
                else {
                    idx += 1
                }
            }
            return@token 0
        }
    }

    private val whiteSpace by regexToken("\\s+", ignore = true)

    // Punctuation and parentheses
    private val comma by regexToken(",")
    private val colon by regexToken(":")
    private val openingBrace by regexToken("\\{")
    private val closingBrace by regexToken("}")
    private val openingBracket by regexToken("\\[")
    private val closingBracket by regexToken("]")

    private val boolToken by regexToken("(true|false)")
    private val nullToken by literalToken("null")
    private val numberToken by regexToken("""-?(\d+)(\.\d+)?([eE][+-]?\d+)?""")
    private val magicTag by regexToken("""<<<START_JSON_ENCODING>>>.*?<<<STOP_JSON_ENCODING>>>""".toRegex(RegexOption.DOT_MATCHES_ALL))

    private val jsonNull: Parser<String> = nullToken asJust "null"
    private val jsonBool: Parser<String> = boolToken use {text}
    private val string: Parser<String> = stringLiteral use {text}
    private val number: Parser<String> = numberToken use {text}
    private val magicString: Parser<String> = magicTag use {text}

    private val jsonPrimitiveValue: Parser<T> = jsonNull or jsonBool or string or number or magicString map { valFunc(it) }
    private val jsonObject: Parser<Any?> = (-openingBrace and
            separated(string and -colon and parser(this::jsonValue), comma, true) and
            -closingBrace)
        .map {
            objectFunc(it.terms as List<Tuple2<String, T>>)
        }
    private val jsonArray: Parser<Any?> = (-openingBracket and
            separated(parser(this::jsonValue), comma, true) and
            -closingBracket)
        .map {
            arrayFunc(it.terms as List<T>)
        }

    private val jsonValue: Parser<Any?> = jsonPrimitiveValue or jsonObject or jsonArray
    override val rootParser = jsonValue as Parser<T>
}


fun coloredJsonParser(keyColor: Color, valueColor: Color): JsonGrammar<ColoredStrings> {
    return JsonGrammar<ColoredStrings>(
        {cs(it, valueColor)},
        {
            cs("[\n$INDENT") + joinCS(it, cs(",\n")).indent() + cs("\n]")
        },
        {
            cs("{\n$INDENT") + joinCS(it, cs(",\n")) { (k, v) -> cs(k, keyColor) + cs(": ") + v }.indent() + cs("\n}")
        }
    )
}

fun coloredJsonParser(): JsonGrammar<ColoredStrings> {
    return coloredJsonParser(Color.YELLOW, Color.GREEN)
}
