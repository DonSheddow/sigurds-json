import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

private val prettyJson = Json { prettyPrint = true }
private const val indent = "    "

fun parseObjectOrArray(x: JsonPrimitive): JsonElement? {
    val regex = Regex("""^ *[\[{]""")
    if (x.isString && regex.containsMatchIn(x.content)) {
        return try {
            Json.parseToJsonElement(x.content)
        } catch (e: SerializationException){
            null
        }
    }
    return null
}

fun prettyPrintWithInnerParse(json: JsonElement, parse: (JsonPrimitive) -> String?): String {
    return when (json) {
        is JsonNull -> "null"
        is JsonPrimitive -> {
            parse(json) ?: json.toString()
        }
        is JsonArray -> {
            "[\n" + json.joinToString(",\n") {
                    x: JsonElement ->
                prettyPrintWithInnerParse(x, parse).lines().joinToString("\n") { indent + it }
            } + "\n]"
        }
        is JsonObject -> {
            "{\n" +
                    json.entries.joinToString(",\n") { (key, value) ->
                        "\"${key}\"" + ": " + prettyPrintWithInnerParse(value, parse)
                    }.lines().joinToString("\n") { indent + it } + "\n}"
        }
    }

}

fun flattenJsonWithMagicTags(json: JsonElement): String {
    return prettyPrintWithInnerParse(json) {
        parseObjectOrArray(it)?.let {
            "<<<START_JSON_ENCODING>>>\n" +
            prettyJson.encodeToString(it) +
            "\n<<<STOP_JSON_ENCODING>>>"
        }
    }
}

fun flattenJson(json: JsonElement): String {
    return prettyPrintWithInnerParse(json) {
        parseObjectOrArray(it)?.let { prettyJson.encodeToString(it) }
    }
}