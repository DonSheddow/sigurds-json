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
        //val input3 = Json.parseToJsonElement("""{"a": "b"}""")
        val output3 = flattenJsonWithMagicTags(Json.parseToJsonElement(input3))
        assertEquals(input3, output3)
    }

    @Test
    fun flattenJsonWithMagicTags2() {
        val input = """{"a": "{\"x\": \"y\"}"}"""
        val expected = """
            {
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
}