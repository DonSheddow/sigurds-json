import burp.api.montoya.core.HighlightColor
import burp.api.montoya.core.MessageAnnotations
import burp.api.montoya.core.ToolSource
import burp.api.montoya.core.ToolType
import burp.api.montoya.http.*
import burp.api.montoya.http.message.requests.HttpRequest
import burp.api.montoya.http.message.responses.HttpResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.util.concurrent.atomic.AtomicBoolean

class MyHTTPHandler(private val http: Http, private val doIntercept: AtomicBoolean) : HttpHandler {
    override fun handleHttpRequest(req: HttpRequest, annotations: MessageAnnotations, src: ToolSource): RequestHandlerResult {
        if (!src.isFromTool(ToolType.REPEATER)) {
            return RequestHandlerResult.from(req, annotations)
        }
        val bodyStr = String(req.body(), Charsets.UTF_8)
        val regex = Regex("""<<<START_JSON_ENCODING>>>(.*?)<<<STOP_JSON_ENCODING>>>""", setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))

        val newBody = regex.replace(bodyStr) {
            val s = it.groups[1]!!.value
            val x = Json.encodeToString(Json.parseToJsonElement(s)) // minify
            Json.encodeToString(x)
        }

        val newReq = http.createRequest(req.httpService(), req.headers().map{it.toString()}, newBody.toByteArray(Charsets.UTF_8))
        return RequestHandlerResult.from(newReq, annotations)
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

        val oldJson = Json.parseToJsonElement(resp.bodyAsString())
        val newJson = flattenJson(oldJson)

        val newResp = http.createResponse(resp.headers().map{it.toString()}, newJson.toString())

        val wasRewritten = oldJson.toString() != newJson.toString()
        return if (wasRewritten) {
            ResponseHandlerResult.from(newResp, annotations.withComment("JSON response has been rewritten").withHighlightColor(
                HighlightColor.BLUE))
        } else {
            ResponseHandlerResult.from(resp, annotations)
        }
    }
}