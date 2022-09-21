import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SimpleJsonGrammarTest {
    @Test
    fun testParse() {
        val input1 = "[1, 2, [3, 4, 5]]"
        val expected1 = """[
            |    1,
            |    2,
            |    [
            |        3,
            |        4,
            |        5
            |    ]
            |]
        """.trimMargin()

        val input2 = "{\"a\": {\"b\": 1}}"
        val expected2 = """{
            |    "a": {
            |        "b": 1
            |    }
            |}
        """.trimMargin()

        assertEquals(expected1, SimpleJsonGrammar.parseToEnd(input1).toPlainString())
        assertEquals(expected2, SimpleJsonGrammar.parseToEnd(input2).toPlainString())
    }
}