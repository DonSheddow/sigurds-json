import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("unused")
class Extension : BurpExtension {
    override fun initialise(api: MontoyaApi) {
        val logging = api.logging()
        val ui = api.userInterface()
        val http = api.http()

        api.misc().setExtensionName("Sigurds Extension")

        val doIntercept = AtomicBoolean(true)

        http.registerHttpHandler(MyHTTPHandler(http, doIntercept))

        ui.registerSuiteTab("Sigurds Extension", SuiteTab(logging, doIntercept))
        ui.registerContextMenuItemsProvider(ContextMenu(logging, http))
    }
}