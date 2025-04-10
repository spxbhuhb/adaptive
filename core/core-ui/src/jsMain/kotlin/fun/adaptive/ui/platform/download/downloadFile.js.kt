package `fun`.adaptive.ui.platform.download

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

actual fun downloadFile(content: ByteArray, fileName: String, mimeType: String) {
    val blob = Blob(arrayOf(content), BlobPropertyBag(type = mimeType))
    val anchor = document.createElement("a") as HTMLAnchorElement
    anchor.href = URL.createObjectURL(blob)
    anchor.download = fileName
    document.body?.appendChild(anchor)
    anchor.click()
    document.body?.removeChild(anchor)
}