package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractBox
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import web.timers.setTimeout

abstract class AbstractClickPopup(
    adapter: AuiAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    val eventName: String
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, parent, index, 3) {

    val focusParentOnHide
        get() = get<Boolean?>(1) == true

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

    val keyDownHandler = { event: Event ->
        if ((event as? KeyboardEvent)?.key == "Escape") {
            hide()
        }
    }

    override fun hide() {
        // this is a trick to let focusout event propagate
        setTimeout({
            super.hide()
            if (focusParentOnHide) { // put focus on the layout receiver on popup hiding
                layoutReceiver?.focus()
            }
        }, 0)
    }

    override fun mount() {
        super.mount()
        receiver.style.position = "fixed"
        layoutReceiver?.addEventListener(eventName, clickHandler)
    }

    override fun configureBox(fragment: AbstractAuiFragment<HTMLElement>) {
        val receiver = fragment.receiver
        receiver.tabIndex = 0
        receiver.addEventListener("focusout", blurHandler)
        receiver.addEventListener("keydown", keyDownHandler)
    }

    override fun unmount() {
        receiver.removeEventListener("keydown", keyDownHandler)
        receiver.removeEventListener("focusout", blurHandler)
        layoutReceiver?.removeEventListener(eventName, clickHandler)
        super.unmount()
    }

}