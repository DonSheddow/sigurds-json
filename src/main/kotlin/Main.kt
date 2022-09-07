import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi
import burp.api.montoya.core.MessageAnnotations
import burp.api.montoya.core.ToolSource
import burp.api.montoya.core.ToolType
import burp.api.montoya.http.*
import burp.api.montoya.http.message.requests.HttpRequest
import burp.api.montoya.http.message.responses.HttpResponse
import burp.api.montoya.logging.Logging
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import java.awt.event.ItemEvent
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*

class MyHTTPHandler(private val http: Http, private val doIntercept: AtomicBoolean) : HttpHandler {
    override fun handleHttpRequest(req: HttpRequest?, annotations: MessageAnnotations, src: ToolSource?): RequestHandlerResult {
        return RequestHandlerResult.from(req, annotations)
    }

    override fun handleHttpResponse(
        req: HttpRequest,
        resp: HttpResponse,
        annotations: MessageAnnotations,
        src: ToolSource
    ): ResponseHandlerResult {
        val mimeType = resp.statedMimeType()
        if (!src.isFromTool(ToolType.REPEATER) || mimeType != MimeType.JSON || !doIntercept.get()) {
            return ResponseHandlerResult.from(resp, annotations)
        }

        val jsonBody = Json.parseToJsonElement(resp.bodyAsString())

        val newJson = traverse(jsonBody) { parseNested(it) }

        val newResp = http.createResponse(resp.headers().map{it.toString()}, newJson.toString())

        return ResponseHandlerResult.from(newResp, annotations)
    }
}

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

@Suppress("unused")
class Extension : BurpExtension {
    override fun initialise(api: MontoyaApi) {
        val logging = api.logging()
        logging.logToOutput("Loaded extension")

        api.misc().setExtensionName("Sigurds Extension")

        val doIntercept = AtomicBoolean(true)

        val http = api.http()
        http.registerHttpHandler(MyHTTPHandler(http, doIntercept))

        SwingUtilities.invokeLater {
            val tab = CheckBoxEx(logging, doIntercept)
            tab.isVisible = true
            api.userInterface().registerSuiteTab("Sigurds Extension", tab)
        }
    }
}


class CheckBoxEx(logging: Logging, doIntercept: AtomicBoolean) : JPanel() {
    init {
        val checkBox = JCheckBox("Rewrite nested JSON", true)

        checkBox.addItemListener { e ->
            val sel: Int = e.stateChange

            if (sel == ItemEvent.SELECTED) {
                logging.logToOutput("Rewriting nested JSON in Repeater")
                doIntercept.set(true)
            } else {
                logging.logToOutput("Rewriting is now disabled")
                doIntercept.set(false)
            }
        }

        add(checkBox)
    }
}