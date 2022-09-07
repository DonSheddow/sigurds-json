import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*

fun parseNested(x: JsonPrimitive): JsonElement {
    val regex = Regex("""^ *[\[{]""")
    if (x.isString && regex.containsMatchIn(x.content)) {
        return try {
            Json.parseToJsonElement(x.content)
        } catch (e: SerializationException){
            x
        }
    }
    return x
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

fun flattenJson(json: JsonElement): JsonElement {
    return traverse(json) { parseNested(it) }
}