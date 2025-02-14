package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.instruction.event.Toggle
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

class AuiContextPopup(
    adapter: AuiAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, parent, index) {

    val clickHandler = { event: Event ->
        val toggle = (instructions.firstInstanceOfOrNull<Toggle>() != null)

        if (toggle && active) {
            hide()
        } else {
            show()
        }

        event.preventDefault()
    }

    override fun mount() {
        super.mount()
        layoutReceiver?.addEventListener("contextmenu", clickHandler)
    }

    override fun unmount() {
        layoutReceiver?.removeEventListener("contextmenu", clickHandler)
        super.unmount()
    }

}