import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi
import burp.api.montoya.ui.editor.extension.EditorMode

@Suppress("unused")
class Extension : BurpExtension {
    override fun initialize(api: MontoyaApi) {
        val logging = api.logging()
        val ui = api.userInterface()
        val http = api.http()

        api.misc().setExtensionName("Sigurds JSON")

        val settings = Settings(true, true)

        http.registerHttpHandler(MyHTTPHandler(http, settings))

        ui.registerSuiteTab("Sigurds JSON", SuiteTab(logging, settings))
        ui.registerContextMenuItemsProvider(ContextMenu(logging, http))
        ui.registerHttpRequestEditorProvider { reqResp, mode ->
            when (mode) {
                EditorMode.DEFAULT -> HttpRequestTab(logging, reqResp, true)
                EditorMode.READ_ONLY -> HttpRequestTab(logging, reqResp, false)
            }
        }
        ui.registerHttpResponseEditorProvider { reqResp, _ ->
            HttpResponseTab(logging, reqResp)
        }
    }
}

data class Settings(var rewriteJson: Boolean, var rewriteXml: Boolean) {
}