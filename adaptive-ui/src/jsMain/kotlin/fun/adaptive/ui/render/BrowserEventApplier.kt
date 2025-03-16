package `fun`.adaptive.ui.render

import `fun`.adaptive.adat.decodeFromJson
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.event.*
import `fun`.adaptive.ui.platform.BrowserEventListener
import org.w3c.dom.DragEvent
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent

object BrowserEventApplier : EventRenderApplier<HTMLElement>() {

    override fun applyNoPointerEvents(fragment: AbstractAuiFragment<HTMLElement>, previous: PointerEvents?, current: PointerEvents?) {
        val style = fragment.receiver.style
        when (current?.enabled) {
            false -> style.setProperty("pointer-events", "none")
            true -> style.setProperty("pointer-events", "auto")
            null -> style.removeProperty("pointer-events")
        }
    }

    override fun addEventListener(fragment: AbstractAuiFragment<HTMLElement>, eventHandler: UIEventHandler): Any {

        val (eventName, condition) = when (eventHandler) {
            is OnClick -> "click" to Always
            is OnDoubleClick -> "dblclick" to Always
            is OnPrimaryDown -> "mousedown" to Primary
            is OnMove -> "mousemove" to Always
            is OnLeave -> "mouseleave" to Always
            is OnPrimaryUp -> "mouseup" to Primary
            is OnDrop -> "drop" to Always
            is OnKeyDown -> "keydown" to Always
            else -> throw UnsupportedOperationException("unsupported event handler: $eventHandler")
        }

        val listener = BrowserEventListener(eventName, condition) { event ->

            if (! this.condition.matches(event)) return@BrowserEventListener

            val target = event.target
            val x: Double
            val y: Double
            val transferData: TransferData?

            if (event is MouseEvent) {
                val boundingRect = fragment.receiver.getBoundingClientRect()
                val renderData = fragment.renderData
                val margin = renderData.layout?.margin ?: RawSurrounding.ZERO
                x = event.clientX - boundingRect.x + margin.start
                y = event.clientY - boundingRect.y + margin.top
            } else {
                x = Double.NaN
                y = Double.NaN
            }

            if (event is DragEvent && event.type == "drop") {
                transferData = event.dataTransfer?.getData("application/json")?.let { TransferData.decodeFromJson(it) }

                if (eventHandler is OnDrop && eventHandler.focusOnDrop && target is HTMLElement) {
                    target.focus()
                }

                event.preventDefault()
            } else {
                transferData = null
            }

            val keyInfo = if (event is KeyboardEvent) {
                event.preventDefault()
                UIEvent.KeyInfo(event.key, event.isComposing, event.repeat)
            } else {
                null
            }

            eventHandler.execute(UIEvent(fragment, event, x, y, transferData, keyInfo, modifiers(event), { event.stopPropagation() }))

            event.preventDefault()
        }

        fragment.receiver.addEventListener(eventName, listener)

        return listener
    }

    fun modifiers(event: Event): Set<EventModifier> {
        val modifiers = mutableSetOf<EventModifier>()

        when (event) {
            is MouseEvent -> {
                if (event.ctrlKey) modifiers.add(EventModifier.CTRL)
                if (event.altKey) modifiers.add(EventModifier.ALT)
                if (event.metaKey) modifiers.add(EventModifier.META)
                if (event.shiftKey) modifiers.add(EventModifier.SHIFT)
                if (event.type == "dblclick") modifiers.add(EventModifier.DOUBLE)
            }

            is KeyboardEvent -> {
                if (event.ctrlKey) modifiers.add(EventModifier.CTRL)
                if (event.altKey) modifiers.add(EventModifier.ALT)
                if (event.metaKey) modifiers.add(EventModifier.META)
                if (event.shiftKey) modifiers.add(EventModifier.SHIFT)
            }
        }

        return modifiers
    }

    override fun removeListener(fragment: AbstractAuiFragment<HTMLElement>, eventListener: Any?) {
        if (eventListener != null) {
            eventListener as BrowserEventListener
            fragment.receiver.removeEventListener(eventListener.eventName, eventListener)
        }
    }

    abstract class EventCondition {
        abstract fun matches(event: Event): Boolean
    }

    object Always : EventCondition() {
        override fun matches(event: Event): Boolean = true
    }

    object Primary : EventCondition() {
        override fun matches(event: Event): Boolean {
            if (event !is MouseEvent) return false
            return (event.button.toInt() == 0)
        }
    }

    object Secondary : EventCondition() {
        override fun matches(event: Event): Boolean {
            if (event !is MouseEvent) return false
            return (event.button.toInt() == 2)
        }
    }
}