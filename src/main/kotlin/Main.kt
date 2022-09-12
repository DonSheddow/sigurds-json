import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi

// TODO: Add tab in proxy for rewritten responses

@Suppress("unused")
class Extension : BurpExtension {
    override fun initialise(api: MontoyaApi) {
        val logging = api.logging()
        val ui = api.userInterface()
        val http = api.http()

        api.misc().setExtensionName("Sigurds Extension")

        val settings = Settings(true, true)

        http.registerHttpHandler(MyHTTPHandler(http, settings))

        ui.registerSuiteTab("Sigurds Extension", SuiteTab(logging, settings))
        ui.registerContextMenuItemsProvider(ContextMenu(logging, http))
    }
}

data class Settings(var rewriteJson: Boolean, var rewriteXml: Boolean) {
}