package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class AuiHoverPopup(
    adapter: AuiAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, parent, index) {

    val enterHandler = { _: Any -> show() }
    val leaveHandler = { _: Any -> hide() }

    override fun mount() {
        super.mount()
        layoutReceiver?.addEventListener("mouseenter", enterHandler)
        layoutReceiver?.addEventListener("mouseleave", leaveHandler)
    }

    override fun unmount() {
        layoutReceiver?.removeEventListener("mouseenter", enterHandler)
        layoutReceiver?.removeEventListener("mouseleave", leaveHandler)
        super.unmount()
    }

}