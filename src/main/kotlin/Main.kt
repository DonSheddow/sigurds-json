import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi
import burp.api.montoya.core.MessageAnnotations
import burp.api.montoya.core.ToolSource
import burp.api.montoya.core.ToolType
import burp.api.montoya.http.*
import burp.api.montoya.http.message.requests.HttpRequest
import burp.api.montoya.http.message.responses.HttpResponse
import burp.api.montoya.logging.Logging
import kotlinx.serialization.json.*

class MyHTTPHandler(private val http: Http, private val logging: Logging) : HttpHandler {
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
        if (!src.isFromTool(ToolType.REPEATER) || mimeType != MimeType.JSON) {
            return ResponseHandlerResult.from(resp, annotations)
        }

        val jsonBody = Json.parseToJsonElement(resp.bodyAsString())

        val newJson = traverse(jsonBody) { f(it) }

        val newResp = http.createResponse(resp.headers().map{it.toString()}, newJson.toString())

        return ResponseHandlerResult.from(newResp, annotations)
    }
}

fun f(x: JsonPrimitive): JsonPrimitive {
    if (x.isString) {
        return JsonPrimitive("hello, world!")
    }
    return x
}

fun traverse(json: JsonElement, func: (JsonPrimitive) -> JsonPrimitive): JsonElement {
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

        val http = api.http()
        http.registerHttpHandler(MyHTTPHandler(http, logging))
    }
}