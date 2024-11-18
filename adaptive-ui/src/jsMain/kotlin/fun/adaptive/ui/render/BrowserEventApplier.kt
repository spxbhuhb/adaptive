package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.OnMove
import `fun`.adaptive.ui.instruction.event.OnPrimaryDown
import `fun`.adaptive.ui.instruction.event.OnPrimaryUp
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.event.UIEventHandler
import `fun`.adaptive.ui.platform.BrowserEventListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

object BrowserEventApplier : EventRenderApplier<HTMLElement>() {

    override fun applyNoPointerEvents(fragment: AbstractAuiFragment<HTMLElement>, previous: Boolean?, current: Boolean?) {
        val style = fragment.receiver.style
        when (current) {
            true -> style.setProperty("pointer-events", "none")
            else -> style.setProperty("pointer-events", "auto")
        }
    }

    override fun addEventListener(fragment: AbstractAuiFragment<HTMLElement>, eventFun: UIEventHandler): Any {

        val (eventName, condition) = when (eventFun) {
            is OnClick -> "click" to Always
            is OnPrimaryDown -> "mousedown" to Primary
            is OnMove -> "mousemove" to Always
            is OnPrimaryUp -> "mouseup" to Primary
            else -> throw UnsupportedOperationException("unsupported event handler: $eventFun")
        }

        val listener = BrowserEventListener(eventName, condition) {

            if (! this.condition.matches(it)) return@BrowserEventListener

            val x: Double
            val y: Double

            if (it is MouseEvent) {
                val boundingRect = fragment.receiver.getBoundingClientRect()
                val renderData = fragment.renderData
                val margin = renderData.layout?.margin ?: RawSurrounding.ZERO
                x = it.clientX - boundingRect.x - margin.start
                y = it.clientY - boundingRect.y - margin.top
            } else {
                x = Double.NaN
                y = Double.NaN
            }

            eventFun.execute(UIEvent(fragment, it, x, y))

        }

        fragment.receiver.addEventListener(eventName, listener)

        return listener
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