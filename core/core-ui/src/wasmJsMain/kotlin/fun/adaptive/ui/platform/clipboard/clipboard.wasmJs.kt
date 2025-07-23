package `fun`.adaptive.ui.platform.clipboard

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.Navigator

actual fun copyToClipboard(text: String) {
    val navigator = navigator()

    // Check if modern Clipboard API is supported
    if ((navigator.clipboard as? JsAny) != null) {
        navigator.clipboard.writeText(text)
    } else {
        fallbackCopy(text)
    }
}

private fun navigator() : Navigator = js("navigator")

fun fallbackCopy(text: String) {
    val textarea = document.createElement("textarea") as HTMLTextAreaElement
    textarea.value = text
    document.body?.appendChild(textarea)
    textarea.select()
    val successful = document.execCommand("copy")
    document.body?.removeChild(textarea)

    if (! successful) {
        window.alert("This browser does not support the copy to clipboard this way. Please copy manually.")
    }
}