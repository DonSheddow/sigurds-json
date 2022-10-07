import burp.api.montoya.http.ContentType
import burp.api.montoya.http.MimeType
import burp.api.montoya.http.message.HttpRequestResponse
import burp.api.montoya.http.message.requests.HttpRequest
import burp.api.montoya.http.message.responses.HttpResponse
import burp.api.montoya.logging.Logging
import burp.api.montoya.ui.Selection
import burp.api.montoya.ui.contextmenu.ContextMenuEvent
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider
import burp.api.montoya.ui.contextmenu.InvocationType
import burp.api.montoya.ui.editor.extension.ExtensionHttpRequestEditor
import burp.api.montoya.ui.editor.extension.ExtensionHttpResponseEditor
import com.github.h0tk3y.betterParse.grammar.tryParseToEnd
import com.github.h0tk3y.betterParse.parser.*
import kotlinx.serialization.json.Json
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
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

class ContextMenu(private val logging: Logging) : ContextMenuItemsProvider {
    override fun provideMenuItems(event: ContextMenuEvent): List<JMenuItem> {
        return if (event.isFrom(InvocationType.MESSAGE_EDITOR_REQUEST)) {
            val menuItem1 = JMenuItem("Parse nested JSON")
            menuItem1.addActionListener {
                val msgEditor = event.messageEditorRequestResponse().get()
                val req = msgEditor.requestResponse.httpRequest()
                val oldJson = Json.parseToJsonElement(String(req.body(), Charsets.UTF_8))
                val newBody = rewriteNestedJsonWithMagicTags(oldJson)
                val newReq = HttpRequest.httpRequest(req.httpService(), req.headers().map{it.toString()}, newBody.toByteArray(Charsets.UTF_8))

                msgEditor.setRequest(newReq)
            }

            val menuItem2 = JMenuItem("Parse nested XML")
            menuItem2.addActionListener {
                val msgEditor = event.messageEditorRequestResponse().get()
                val req = msgEditor.requestResponse.httpRequest()
                val oldJson = Json.parseToJsonElement(String(req.body(), Charsets.UTF_8))
                val newBody = rewriteXmlInJsonWithMagicTags(oldJson)
                val newReq = HttpRequest.httpRequest(req.httpService(), req.headers().map{it.toString()}, newBody.toByteArray(Charsets.UTF_8))

                msgEditor.setRequest(newReq)
            }

            listOf(menuItem1, menuItem2)
        } else {
            emptyList()
        }
    }
}


abstract class HttpTab(private val logging: Logging, editable: Boolean)  {
    val jsonEditor = JsonEditor(logging)
    private var changed = false

    init {
        jsonEditor.textPane.isEditable = editable
        jsonEditor.textPane.document.addDocumentListener(object: DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                changed = true
            }

            override fun removeUpdate(e: DocumentEvent?) {
                changed = true
            }

            override fun changedUpdate(e: DocumentEvent?) {
                changed = true
            }

        })
    }

    fun caption(): String {
        return "Sigurds JSON"
    }

    fun uiComponent(): Component {
        return jsonEditor
    }

    fun selectedData(): Selection {
        TODO("Not yet implemented")
    }

    fun isModified(): Boolean {
        return changed
    }

}

class HttpResponseTab(logging: Logging, private val requestResponse: HttpRequestResponse) : HttpTab(logging, false), ExtensionHttpResponseEditor {
    override fun setHttpRequestResponse(requestResponse: HttpRequestResponse) {
        val body = requestResponse.httpResponse().bodyAsString()
        jsonEditor.updateBody(body)
    }

    override fun isEnabledFor(requestResponse: HttpRequestResponse): Boolean {
        val resp = requestResponse.httpResponse()
        return resp.inferredMimeType() == MimeType.JSON || resp.statedMimeType() == MimeType.JSON
    }

    override fun getHttpResponse(): HttpResponse {
        val resp = requestResponse.httpResponse()
        return HttpResponse.httpResponse(resp.headers().map{it.toString()}, jsonEditor.textPane.text)
    }
}


class HttpRequestTab(private val logging: Logging, private val requestResponse: HttpRequestResponse, editable: Boolean) : HttpTab(logging, editable), ExtensionHttpRequestEditor {
    override fun setHttpRequestResponse(requestResponse: HttpRequestResponse) {
        val body = requestResponse.httpRequest().bodyAsString()
        jsonEditor.updateBody(body)
    }

    override fun isEnabledFor(requestResponse: HttpRequestResponse): Boolean {
        val req = requestResponse.httpRequest()
        return req.body().isNotEmpty() && req.contentType() == ContentType.JSON
    }

    override fun getHttpRequest(): HttpRequest {
        val req = requestResponse.httpRequest()
        val text = normalize(jsonEditor.textPane.text)
        return HttpRequest.httpRequest(req.httpService(), req.headers().map{it.toString()}, text)
    }
}

class JsonEditor(private val logging: Logging) : JPanel(BorderLayout()) {
    val textPane = JTextPane()
    private val doc = textPane.styledDocument
    private val style = textPane.addStyle("style", null)
    private val scrollPane = JScrollPane(textPane)

    init {
        val button = JButton("Parse nested JSON")
        button.addActionListener {
            val oldJson = Json.parseToJsonElement(textPane.text)
            val newText = rewriteNestedJsonWithMagicTags(oldJson)
            insertBody(newText)
        }
        button.border = EmptyBorder(5, 5, 5, 5)
        add(button, BorderLayout.PAGE_START)
        add(scrollPane, BorderLayout.CENTER)
    }

    private fun getColors(): Pair<Color, Color> {
        return if (background.red < 127) { // dark mode
            Pair(Color(199, 166, 0), Color(120, 199, 199))
        }
        else { // light mode
            Pair(Color(2, 4, 94), Color(4, 110, 10))
        }
    }

    fun updateBody(s: String) {
        if (normalize(s) == normalize(textPane.text)) {
            return
        }
        insertBody(s)
    }

    private fun insertBody(s: String) {
        val (k, v) = getColors()
        val cs = when (val res = coloredJsonParser(k, v).tryParseToEnd(s)) {
            is Parsed -> res.value
            is ErrorResult -> {
                logging.logToError("Unable to parse JSON: " + res.javaClass.canonicalName)
                return
            }
        }

        textPane.text = ""

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

fun normalize(s: String): String {
    return processMagicTags(s.replace("\r\n", "\n"), true)
}