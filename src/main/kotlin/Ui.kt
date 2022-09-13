import burp.api.montoya.http.Http
import burp.api.montoya.logging.Logging
import burp.api.montoya.ui.contextmenu.ContextMenuEvent
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider
import burp.api.montoya.ui.contextmenu.InvocationType
import kotlinx.serialization.json.Json
import java.awt.event.ItemEvent
import javax.swing.*

class SuiteTab(private val logging: Logging, private val settings: Settings) : JPanel() {
    init {
        isVisible = true
        val checkBox1 = JCheckBox("Rewrite nested JSON in Repeater", settings.rewriteJson)
        val checkBox2 = JCheckBox("Rewrite nested XML in Repeater", settings.rewriteXml)

        checkBox1.addItemListener { e ->
            val sel: Int = e.stateChange

            if (sel == ItemEvent.SELECTED) {
                logging.logToOutput("Rewriting nested JSON in Repeater")
                settings.rewriteJson = true
            } else {
                logging.logToOutput("JSON rewriting is now disabled")
                settings.rewriteJson = false
            }
        }

        checkBox2.addItemListener { e ->
            val sel: Int = e.stateChange

            if (sel == ItemEvent.SELECTED) {
                logging.logToOutput("Rewriting nested XML in Repeater")
                settings.rewriteXml = true
            } else {
                logging.logToOutput("XML rewriting is now disabled")
                settings.rewriteXml = false
            }
        }

        add(checkBox1)
        add(checkBox2)
    }
}

class ContextMenu(private val logging: Logging, private val http: Http) : ContextMenuItemsProvider {
    override fun provideMenuItems(event: ContextMenuEvent): List<JMenuItem> {
        return if (event.isFrom(InvocationType.MESSAGE_EDITOR_REQUEST)) {
            val menuItem1 = JMenuItem("Parse nested JSON")
            menuItem1.addActionListener {
                val msgEditor = event.messageEditorRequestResponse().get()
                val req = msgEditor.requestResponse.httpRequest()
                val oldJson = Json.parseToJsonElement(String(req.body(), Charsets.UTF_8))
                val newBody = rewriteNestedJsonWithMagicTags(oldJson)
                val newReq = http.createRequest(req.httpService(), req.headers().map{it.toString()}, newBody.toByteArray(Charsets.UTF_8))

                msgEditor.setRequest(newReq)
            }

            val menuItem2 = JMenuItem("Parse nested XML")
            menuItem2.addActionListener {
                val msgEditor = event.messageEditorRequestResponse().get()
                val req = msgEditor.requestResponse.httpRequest()
                val oldJson = Json.parseToJsonElement(String(req.body(), Charsets.UTF_8))
                val newBody = rewriteXmlInJsonWithMagicTags(oldJson)
                val newReq = http.createRequest(req.httpService(), req.headers().map{it.toString()}, newBody.toByteArray(Charsets.UTF_8))

                msgEditor.setRequest(newReq)
            }

            listOf(menuItem1, menuItem2)
        } else {
            emptyList()
        }
    }
}