import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.lexer.token
import com.github.h0tk3y.betterParse.parser.Parser
import java.awt.Color

class ColoredStrings(val strings: List<Pair<String, Color?>>) {
    operator fun plus(other: ColoredStrings): ColoredStrings {
        return ColoredStrings(this.strings + other.strings)
    }

    fun indent(): ColoredStrings {
        return ColoredStrings(strings.map {(a, b) -> Pair(a.replace("\n", "\n" + INDENT), b) } )
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


object SimpleJsonGrammar : Grammar<ColoredStrings>() {
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

    private val jsonNull: Parser<String> = nullToken asJust "null"
    private val jsonBool: Parser<String> = boolToken use {text}
    private val string: Parser<String> = stringLiteral use {text}
    private val number: Parser<String> = numberToken use {text}

    private val jsonPrimitiveValue: Parser<ColoredStrings> = jsonNull or jsonBool or string or number map { cs(it, Color.GREEN) }
    private val jsonObject: Parser<ColoredStrings> = (-openingBrace and
            separated(string and -colon and parser(this::jsonValue), comma, true) and
            -closingBrace)
        .map {
            cs("{\n$INDENT") + joinCS(it.terms, cs(",\n")) { (k, v) -> cs(k, Color.YELLOW) + cs(": ") + v }.indent() + cs("\n}")
        }
    private val jsonArray: Parser<ColoredStrings> = (-openingBracket and
            separated(parser(this::jsonValue), comma, true) and
            -closingBracket)
        .map {
            cs("[\n$INDENT") + joinCS(it.terms, cs(",\n")).indent() + cs("\n]")
        }
    private val jsonValue: Parser<ColoredStrings> = jsonPrimitiveValue or jsonObject or jsonArray
    override val rootParser = jsonValue
}