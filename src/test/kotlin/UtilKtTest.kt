import kotlinx.serialization.json.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UtilKtTest {

    @Test
    fun flattenJsonWithMagicTags1() {
        val input = JsonNull
        val output = flattenJsonWithMagicTags(input)
        assertEquals("null", output)

        val input2 = """[
            |    "hello",
            |    "world"
            |]""".trimMargin()
        val output2 = flattenJsonWithMagicTags(Json.parseToJsonElement(input2))
        assertEquals(input2, output2)

        val input3 = """{
            |    "a": "b"
            |}""".trimMargin()
        val output3 = flattenJsonWithMagicTags(Json.parseToJsonElement(input3))
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

        val output = flattenJsonWithMagicTags(Json.parseToJsonElement(input))
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
        val output = flattenJson(Json.parseToJsonElement(input))
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
        val output = flattenJson(Json.parseToJsonElement(input))
        assertEquals(expected, output)
    }
}