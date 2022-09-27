import burp.api.montoya.http.Http
import burp.api.montoya.http.message.HttpRequestResponse
import burp.api.montoya.http.message.requests.HttpRequest
import burp.api.montoya.logging.Logging
import burp.api.montoya.ui.Selection
import burp.api.montoya.ui.contextmenu.ContextMenuEvent
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider
import burp.api.montoya.ui.contextmenu.InvocationType
import burp.api.montoya.ui.editor.extension.ExtensionHttpRequestEditor
import com.github.h0tk3y.betterParse.grammar.tryParseToEnd
import com.github.h0tk3y.betterParse.parser.*
import kotlinx.serialization.json.Json
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.text.StyleConstants


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


class HttpRequestTab(private val logging: Logging) : ExtensionHttpRequestEditor {
    private val jsonEditor = JsonEditor(logging)

    override fun setHttpRequestResponse(requestResponse: HttpRequestResponse) {
        val body = requestResponse.httpRequest().bodyAsString()
        jsonEditor.updateBody(body)
    }

    override fun isEnabledFor(requestResponse: HttpRequestResponse): Boolean {
        val body = requestResponse.httpRequest().bodyAsString()
        return body.isNotEmpty()
    }

    override fun caption(): String {
        return "Sigurds JSON"
    }

    override fun uiComponent(): Component {
        return jsonEditor
    }

    override fun selectedData(): Selection {
        TODO("Not yet implemented")
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun getHttpRequest(): HttpRequest {
        TODO("Not yet implemented")
    }
}

class JsonEditor(private val logging: Logging) : JPanel(BorderLayout()) {
    private val textPane = JTextPane()
    private val doc = textPane.styledDocument
    private val style = textPane.addStyle("style", null)
    private val scrollPane = JScrollPane(textPane)

    init {
        add(scrollPane, BorderLayout.CENTER)
        scrollPane.scrollRectToVisible(textPane.visibleRect)
    }

    fun updateBody(s: String) {
        val cs = when (val res = SimpleJsonGrammar.tryParseToEnd(s)) {
            is Parsed -> res.value
            is ErrorResult -> {
                logging.logToError("Unable to parse JSON: " + res.javaClass.canonicalName)
                return
            }
        }

        if (cs.toPlainString() == textPane.text.replace("\r\n", "\n")) {
            return
        }

        for ((str, color) in cs.strings) {
            if (color != null) {
                StyleConstants.setForeground(style, color)
                doc.insertString(doc.length, str, style)
            }
            else {
                doc.insertString(doc.length, str, null)
            }
        }
        textPane.caretPosition = 0
    }
}