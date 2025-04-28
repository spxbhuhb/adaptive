package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractBox
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.KeyboardEvent

abstract class AbstractClickPopup(
    adapter: AuiAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    val eventName: String
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, parent, index, 3) {

    override val modal: Boolean
        get() = true

    val sourceViewBackend
        get() = get<PopupSourceViewBackend?>(1)

    val blurHandler = { event: Any ->
        event as FocusEvent
        val target = event.relatedTarget as? HTMLElement

        // This makes input fields in popup work. I don't see why, but when an input field is inside the popup,
        // the browser sends a focusout event on input focus. Most probably there is a reason for this,
        // but I don't have the time to investigate it right now.

        // So, this check looks up the event target between the children and keeps the popup open if the
        // focus is actually on one of the children.

        val inside = this.firstOrNull { it is AbstractAuiFragment<*> && it.receiver === target }

        if (inside == null && instructions.firstInstanceOfOrNull<PopupAlign>()?.modal != true) {
            super.hide()

            sourceViewBackend?.let { it.isPopupOpen = false }

            if (sourceViewBackend?.focusContainerOnPopupFocusOut == true) {
                layoutReceiver?.focus()
            }
        }
    }

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

    override fun buildContainer(parent: AdaptiveFragment): AbstractBox<HTMLElement, HTMLDivElement> {
        val container = super.buildContainer(parent)
        container.receiver.addEventListener("focusout", blurHandler)
        container.receiver.addEventListener("keydown", keyDownHandler)
        return container
    }

    override fun show() {
        sourceViewBackend?.let { it.isPopupOpen = true }
        super.show()
    }

    override fun hide() {
        super.hide()

        sourceViewBackend?.let { it.isPopupOpen = false }

        if (sourceViewBackend?.focusContainerOnPopupClose == true) {
            layoutReceiver?.focus()
        }
    }

    override fun mount() {
        super.mount()
        layoutReceiver?.addEventListener(eventName, clickHandler)
    }


    override fun unmount() {
        layoutReceiver?.removeEventListener(eventName, clickHandler)
        super.unmount()
    }

}