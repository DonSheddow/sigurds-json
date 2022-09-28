import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi

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
        ui.registerHttpRequestEditorProvider { _, _ ->
            HttpRequestTab(logging)
        }
        ui.registerHttpResponseEditorProvider { _, _ ->
            HttpResponseTab(logging)
        }
    }
}

data class Settings(var rewriteJson: Boolean, var rewriteXml: Boolean) {
}