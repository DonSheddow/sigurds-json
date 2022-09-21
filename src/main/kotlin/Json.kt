import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separated
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import java.awt.Color

object SimpleJsonGrammar : Grammar<ColoredStrings>() {
    private val whiteSpace by regexToken("\\s+", ignore = true)

    private val comma by literalToken(",")
    private val colon by literalToken(":")
    private val openingBrace by literalToken("{")
    private val closingBrace by literalToken("}")
    private val openingBracket by literalToken("[")
    private val closingBracket by literalToken("]")

    private val jsonNull by literalToken("null")
    private val jsonBool by regexToken("""(true|false)""")
    private val jsonNumber by regexToken("""-?(\d+)(\.\d+)?([eE][+-]?\d+)?""")
    private val stringLiteral by regexToken(""""[^"]*"""")

    private val jsonPrimitiveValue: Parser<ColoredStrings> = (jsonNull or jsonBool or jsonNumber or stringLiteral) use { cs(text, Color.BLUE) }
    private val jsonArray: Parser<ColoredStrings> = (-openingBracket and
            separated(parser(this::jsonValue), comma, true) and
            -closingBracket)
        .map {
            cs("[\n$INDENT") + joinCS(it.terms, cs(",\n")).indent() + cs("\n]")
        }
    private val jsonObject: Parser<ColoredStrings> = (-openingBrace and
            separated(stringLiteral and -colon and parser(this::jsonValue), comma, true) and
            -closingBrace)
        .map {
            cs("{\n$INDENT") + joinCS(it.terms, cs(",\n")) { (k, v) -> cs(k.text + ": ") + v }.indent() + cs("\n}")
        }
    private val jsonValue: Parser<ColoredStrings> = jsonPrimitiveValue or jsonObject or jsonArray
    override val rootParser = jsonValue
}

class ColoredStrings(private val strings: List<Pair<String, Color?>>) {
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

fun joinCS(l: List<ColoredStrings>, separator: ColoredStrings): ColoredStrings {
    return joinCS(l, separator) {it}
}

fun cs(str: String, color: Color?): ColoredStrings = ColoredStrings(listOf(Pair(str, color)))
fun cs(str: String): ColoredStrings = cs(str, null)