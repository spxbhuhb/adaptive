package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractBox
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

abstract class AbstractClickPopup(
    adapter: AuiAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    val eventName : String
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, parent, index) {

    val blurHandler = { _: Any -> hide() }

    val clickHandler = { event: Event ->
        if (active) {
            hide()
        } else {
            show()
            this.firstOrNull<AbstractBox<HTMLElement, HTMLDivElement>>()?.receiver?.focus()
        }

        event.preventDefault()
    }

    override fun mount() {
        super.mount()
        receiver.style.position = "fixed"
        layoutReceiver?.addEventListener(eventName, clickHandler)
    }

    override fun configureBox(fragment: AbstractAuiFragment<HTMLElement>) {
        val receiver = fragment.receiver
        receiver.tabIndex = 0
        receiver.addEventListener("blur", blurHandler)
    }

    override fun unmount() {
        receiver.removeEventListener("blur", blurHandler)
        layoutReceiver?.removeEventListener(eventName, clickHandler)
        super.unmount()
    }

}