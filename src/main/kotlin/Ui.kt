import burp.api.montoya.http.Http
import burp.api.montoya.logging.Logging
import burp.api.montoya.ui.contextmenu.ContextMenuEvent
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider
import burp.api.montoya.ui.contextmenu.InvocationType
import kotlinx.serialization.json.Json
import java.awt.event.ItemEvent
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JCheckBox
import javax.swing.JMenuItem
import javax.swing.JPanel

class SuiteTab(logging: Logging, doIntercept: AtomicBoolean) : JPanel() {
    init {
        isVisible = true
        val checkBox = JCheckBox("Rewrite nested JSON in Repeater", true)

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

class ContextMenu(private val logging: Logging, private val http: Http) : ContextMenuItemsProvider {
    override fun provideMenuItems(event: ContextMenuEvent): List<JMenuItem> {
        return if (event.isFrom(InvocationType.MESSAGE_EDITOR_REQUEST)) {
            val menuItem = JMenuItem("Parse nested JSON")
            menuItem.addActionListener {
                val msgEditor = event.messageEditorRequestResponse().get()
                val req = msgEditor.requestResponse.httpRequest()
                val oldJson = Json.parseToJsonElement(req.bodyAsString())
                val newBody = flattenJsonWithMagicTags(oldJson)

                val newReq = http.createRequest(req.httpService(), req.headers().map{it.toString()}, newBody)

                msgEditor.setRequest(newReq)
            }

            listOf(menuItem)
        } else {
            emptyList()
        }
    }
}
