import kotlinx.serialization.json.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UtilKtTest {

    @Test
    fun flattenJsonWithMagicTags1() {
        val input = JsonNull
        val output = rewriteNestedJsonWithMagicTags(input)
        assertEquals("null", output)

        val input2 = """[
            |    "hello",
            |    "world"
            |]""".trimMargin()
        val output2 = rewriteNestedJsonWithMagicTags(Json.parseToJsonElement(input2))
        assertEquals(input2, output2)

        val input3 = """{
            |    "a": "b"
            |}""".trimMargin()
        val output3 = rewriteNestedJsonWithMagicTags(Json.parseToJsonElement(input3))
        assertEquals(input3, output3)
    }

    @Test
    fun flattenJsonWithMagicTags2() {
        val input = """{"b": 123, "a": "{\"x\": \"y\"}"}"""
        val expected = """
            {
                "b": 123,
                "a": <<<START_JSON_ENCODING>>>
                {
                    "x": "y"
                }
                <<<STOP_JSON_ENCODING>>>
            }
        """.trimIndent()

        val output = rewriteNestedJsonWithMagicTags(Json.parseToJsonElement(input))
        assertEquals(expected, output)
    }

    @Test
    fun flattenJson1() {
        val input = """{"x": 1, "a": {"b": {"c": 42}}, "nested": "{\"hello\": \"world\"}"}"""
        val expected = """
            {
                "x": 1,
                "a": {
                    "b": {
                        "c": 42
                    }
                },
                "nested": {
                    "hello": "world"
                }
            }
        """.trimIndent()
        val output = rewriteNestedJson(Json.parseToJsonElement(input))
        assertEquals(expected, output)
    }
    @Test
    fun flattenJson2() {
        val input = """[123, "hello", [1, 2, 3], "[\"a\", \"b\", \"c\"]"]"""
        val expected = """
            [
                123,
                "hello",
                [
                    1,
                    2,
                    3
                ],
                [
                    "a",
                    "b",
                    "c"
                ]
            ]
        """.trimIndent()
        val output = rewriteNestedJson(Json.parseToJsonElement(input))
        assertEquals(expected, output)
    }

    @Test
    fun rewriteXmlInJson() {
        val input = """{"a": "b", "xml": "<a>\n<b></b>\n</a>"}"""
        val expected = """
            {
                "a": "b",
                "xml": <a>
                <b></b>
                </a>
            }
        """.trimIndent()
        val output = rewriteXmlInJson(Json.parseToJsonElement(input))
        assertEquals(expected, output)
    }

    @Test
    fun rewriteXmlInJsonWithMagicTags() {
        val input = """{"a": "b", "xml": "<a>\n<b></b>\n</a>"}"""
        val expected = """
            {
                "a": "b",
                "xml": <<<START_JSON_ENCODING>>>
                <a>
                <b></b>
                </a>
                <<<STOP_JSON_ENCODING>>>
            }
        """.trimIndent()
        val output = rewriteXmlInJsonWithMagicTags(Json.parseToJsonElement(input))
        assertEquals(expected, output)
    }
}