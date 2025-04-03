package `fun`.adaptive.ui.virtualized

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual
class AuiVirtualized(
    adapter: AbstractAuiAdapter<HTMLElement, HTMLDivElement>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractVirtualized<HTMLElement, HTMLDivElement>(
    adapter, parent, declarationIndex
) {
    override val spacerHeight
        get() = 10000.0

    val spacer = document.createElement("div") as HTMLElement
    val itemContainer = document.createElement("div") as HTMLElement

    init {
        receiver.style.overflowY = "auto"
        receiver.addEventListener("scroll", { onScroll(receiver.scrollTop) })

        with(spacer.style) {
            width = "100%"
            minHeight = "${spacerHeight}px"
            height = "${spacerHeight}px"
        }

        receiver.appendChild(spacer)
        receiver.appendChild(itemContainer)

        itemContainer.appendChild(document.createElement("span").also { it.innerHTML = "Hello&nbsp;World!" })
    }

    override fun auiPatchInternal() {

    }

    override fun moveViewport(viewportOffset: Double) {
        spacer.scrollTop = viewportOffset
    }

    override fun moveItem(itemOffset: Double, itemIndex: Int) {
        layoutItems[itemIndex].receiver.style.transform = "translateY($itemOffset)"
    }


}