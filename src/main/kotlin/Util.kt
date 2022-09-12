import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

private val prettyJson = Json { prettyPrint = true }
private const val indent = "    "

fun parseObjectOrArray(s: String): JsonElement? {
    val regex = Regex("""^\s*[\[{]""")
    if (regex.containsMatchIn(s)) {
        return try {
            Json.parseToJsonElement(s)
        } catch (e: SerializationException) {
            null
        }
    }
    return null
}

fun prettyPrintWithInnerParse(json: JsonElement, parse: (String) -> String?): String {
    return when (json) {
        is JsonNull -> "null"
        is JsonPrimitive -> {
            if (json.isString) {
                parse(json.content) ?: json.toString()
            } else {
                json.toString()
            }
        }

        is JsonArray -> {
            "[\n" + json.joinToString(",\n") { x ->
                prettyPrintWithInnerParse(x, parse)
            }.lines().joinToString("\n") { indent + it } + "\n]"
        }

        is JsonObject -> {
            "{\n" + json.entries.joinToString(",\n") { (key, value) ->
                "\"${key}\"" + ": " + prettyPrintWithInnerParse(value, parse)
            }.lines().joinToString("\n") { indent + it } + "\n}"
        }
    }
}

fun rewriteNestedJsonWithMagicTags(json: JsonElement): String {
    return prettyPrintWithInnerParse(json) { s ->
        parseObjectOrArray(s)?.let { elem ->
            "<<<START_JSON_ENCODING>>>\n" +
                    prettyJson.encodeToString(elem) +
                    "\n<<<STOP_JSON_ENCODING>>>"
        }
    }
}

fun rewriteNestedJson(json: JsonElement): String {
    return prettyPrintWithInnerParse(json) { s ->
        parseObjectOrArray(s)?.let { prettyJson.encodeToString(it) }
    }
}

// TODO: better heuristic for XML in JSON
fun rewriteXmlInJson(json: JsonElement): String {
    return prettyPrintWithInnerParse(json) { s ->
        if (looksLikeXml(s)) {
            s
        } else {
            null
        }
    }
}

fun rewriteXmlInJsonWithMagicTags(json: JsonElement): String {
    return prettyPrintWithInnerParse(json) { s ->
        if (looksLikeXml(s)) {
            "<<<START_JSON_ENCODING>>>\n" +
                    s +
                    "\n<<<STOP_JSON_ENCODING>>>"
        } else {
            null
        }
    }
}

fun looksLikeXml(s: String): Boolean {
    val regexStart = Regex("""^\s*<""")
    val regexEnd = Regex(""">\s*$""")
    return regexStart.containsMatchIn(s) && regexEnd.containsMatchIn(s)
}

fun tryMinifyJson(s: String): String? {
    return try {
        Json.encodeToString(Json.parseToJsonElement(s))
    }
    catch (e: SerializationException) {
        null
    }
}