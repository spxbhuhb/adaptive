package `fun`.adaptive.ui.platform.clipboard

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLTextAreaElement

actual fun copyToClipboard(text: String) {
    val navigator = js("navigator")

    // Check if modern Clipboard API is supported
    if (navigator.clipboard != undefined) {
        navigator.clipboard.writeText(text).catch {
            console.warn("Clipboard API failed, falling back...")
            fallbackCopy(text)
        }
    } else {
        fallbackCopy(text)
    }
}

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