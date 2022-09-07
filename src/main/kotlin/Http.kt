import burp.api.montoya.core.HighlightColor
import burp.api.montoya.core.MessageAnnotations
import burp.api.montoya.core.ToolSource
import burp.api.montoya.core.ToolType
import burp.api.montoya.http.*
import burp.api.montoya.http.message.requests.HttpRequest
import burp.api.montoya.http.message.responses.HttpResponse
import kotlinx.serialization.json.*
import java.util.concurrent.atomic.AtomicBoolean

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