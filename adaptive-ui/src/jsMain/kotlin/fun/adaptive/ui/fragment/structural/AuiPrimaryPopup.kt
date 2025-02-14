package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.instruction.event.Toggle
import `fun`.adaptive.ui.render.BrowserEventApplier
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

class AuiPrimaryPopup(
    adapter: AuiAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, parent, index) {

    val clickHandler = { event: Event ->
        if (BrowserEventApplier.Primary.matches(event)) {

            val toggle = (instructions.firstInstanceOfOrNull<Toggle>() != null)
            if (toggle && active) {
                hide()
            } else {
                show()
            }
            event.preventDefault()
        }
    }

    override fun mount() {
        super.mount()
        layoutReceiver?.addEventListener("click", clickHandler)
    }

    override fun unmount() {
        layoutReceiver?.removeEventListener("click", clickHandler)
        super.unmount()
    }

}