import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

fun tryParseJson(x: JsonPrimitive): JsonElement? {
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

fun traverse(json: JsonElement, func: (JsonPrimitive) -> JsonElement): JsonElement {
    return when (json) {
        is JsonNull -> json
        is JsonPrimitive -> func(json)
        is JsonArray -> JsonArray(json.map { x: JsonElement -> traverse(x, func) })
        is JsonObject -> JsonObject(json.entries.associate { (key, value) ->
            key to traverse(value, func)
        })
    }
}


private val prettyJson = Json { prettyPrint = true }
private val indent = "    "

fun flattenJsonWithMagicTags(json: JsonElement): String {
    return when (json) {
        is JsonNull -> "null"
        is JsonPrimitive -> {
            val x = tryParseJson(json)
            if (x != null) {
                "<<<START_JSON_ENCODING>>>\n" +
                prettyJson.encodeToString(x) +
                "\n<<<STOP_JSON_ENCODING>>>"
            }
            else {
                json.toString()
            }
        }
        is JsonArray -> {
            "[\n" + json.joinToString(",\n") {
                    x: JsonElement ->
                flattenJsonWithMagicTags(x).lines().joinToString { indent + it }
            } + "\n]"
        }
        is JsonObject -> {
            "{\n" +
            json.entries.map { (key, value) ->
                indent + "\"${key}\"" + ": " + flattenJsonWithMagicTags(value)
            }.joinToString(",\n") + "\n}"
        }
    }
}

fun flattenJson(json: JsonElement): JsonElement {
    return traverse(json) { tryParseJson(it) ?: it }
}