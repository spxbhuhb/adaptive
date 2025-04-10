package `fun`.adaptive.ui.platform

import `fun`.adaptive.runtime.getPlatformType
import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement

fun getScrollbarWidth(): Double {

    val inner = document.createElement("p") as HTMLParagraphElement
    inner.style.width = "100%"
    inner.style.height = "200px"

    val outer = document.createElement("div") as HTMLDivElement
    outer.style.position = "absolute"
    outer.style.top = "0px"
    outer.style.left = "0px"
    outer.style.visibility = "hidden"
    outer.style.width = "200px"
    outer.style.height = "150px"
    outer.style.setProperty("overflow", "hidden")
    outer.appendChild(inner)

    document.body?.appendChild(outer)

    val w1 = inner.offsetWidth
    outer.style.setProperty("overflow", "scroll")
    var w2 = inner.offsetWidth
    if (w1 == w2) {
        w2 = outer.clientWidth
    }

    document.body?.removeChild(outer)

    return (w1 - w2).toDouble()
}

fun applyCustomScrollBar(element : HTMLElement) {
    val platformType = getPlatformType()
    if (!platformType.isMac) {
        element.addClass(".custom-scrollbar")
    }
}