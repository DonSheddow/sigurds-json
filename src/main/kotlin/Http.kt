import burp.api.montoya.core.Annotations
import burp.api.montoya.core.ToolSource
import burp.api.montoya.core.ToolType
import burp.api.montoya.http.*
import burp.api.montoya.http.message.requests.HttpRequest
import burp.api.montoya.http.message.responses.HttpResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

class MyHTTPHandler(private val settings: Settings) : HttpHandler {
    override fun handleHttpRequest(req: HttpRequest, annotations: Annotations, src: ToolSource): RequestResult {
        if (!src.isFromTool(ToolType.REPEATER)) {
            return RequestResult.requestResult(req, annotations)
        }
        val bodyStr = String(req.body(), Charsets.UTF_8)
        val regex = Regex("""<<<START_JSON_ENCODING>>>(.*?)<<<STOP_JSON_ENCODING>>>""", setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))

        val newBody = regex.replace(bodyStr) {
            val s = it.groups[1]!!.value
            Json.encodeToString(tryMinifyJson(s) ?: s)
        }

        val newReq = HttpRequest.httpRequest(req.httpService(), req.headers().map{it.toString()}, newBody.toByteArray(Charsets.UTF_8))
        return RequestResult.requestResult(newReq, annotations)
    }

    override fun handleHttpResponse(
        req: HttpRequest,
        resp: HttpResponse,
        annotations: Annotations,
        src: ToolSource
    ): ResponseResult {
        val mimeType = resp.statedMimeType()
        if (!src.isFromTool(ToolType.REPEATER) || mimeType != MimeType.JSON || !settings.rewriteJson) {
            return ResponseResult.responseResult(resp, annotations)
        }

        var body = resp.bodyAsString()

        if (settings.rewriteJson) {
            body = rewriteNestedJson(Json.parseToJsonElement(body))
        }
        if (settings.rewriteXml) {
            body = rewriteXmlInJson(Json.parseToJsonElement(body))
        }

        val newResp = HttpResponse.httpResponse(resp.headers().map{it.toString()}, body)

        return ResponseResult.responseResult(newResp, annotations)
    }
}