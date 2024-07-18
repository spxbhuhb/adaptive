package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.layout.RawSurrounding
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.BrowserEventListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent

object BrowserEventApplier : EventRenderApplier<HTMLElement>() {

    override fun addEventListener(fragment: AbstractCommonFragment<HTMLElement>, eventFun: UIEventHandler): Any {

        val eventName = when (eventFun) {
            is OnClick -> "click"
            is OnCursorDown -> "mousedown"
            is OnCursorMove -> "mousemove"
            is OnCursorUp -> "mouseup"
            else -> throw UnsupportedOperationException("unsupported event handler: $eventFun")
        }

        val listener = BrowserEventListener(eventName) {

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

    override fun removeListener(fragment: AbstractCommonFragment<HTMLElement>, eventListener: Any?) {
        if (eventListener != null) {
            eventListener as BrowserEventListener
            fragment.receiver.removeEventListener(eventListener.eventName, eventListener)
        }
    }

}