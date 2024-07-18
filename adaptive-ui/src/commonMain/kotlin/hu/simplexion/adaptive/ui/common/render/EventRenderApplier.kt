package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.UIEventHandler

abstract class EventRenderApplier<R> : AbstractRenderApplier() {

    fun applyTo(fragment: AbstractCommonFragment<R>) {
        val previousData = fragment.previousRenderData
        val currentData = fragment.renderData

        val previous = previousData.event
        val current = currentData.event

        if (previous == current) return

        applyEventHandler(
            fragment,
            previous?.onClick,
            previous?.onClickListener,
            current?.onClick
        ).also {
            if (current != null) current.onClickListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onCursorDown,
            previous?.onCursorDownListener,
            current?.onCursorDown
        ).also {
            if (current != null) current.onCursorDownListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onCursorMove,
            previous?.onCursorMoveListener,
            current?.onCursorMove
        ).also {
            if (current != null) current.onCursorMoveListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onCursorUp,
            previous?.onCursorUpListener,
            current?.onCursorUp
        ).also {
            if (current != null) current.onCursorUpListener = it
        }

    }

    fun applyEventHandler(
        fragment: AbstractCommonFragment<R>,
        previousFun: UIEventHandler?,
        previousListener: Any?,
        currentFun: UIEventHandler?
    ): Any? {

        if (previousFun == currentFun) {
            return previousListener
        }

        if (previousListener != null) {
            removeListener(fragment, previousListener)
        }

        if (currentFun != null) {
            return addEventListener(fragment, currentFun)
        } else {
            return null
        }
    }

    abstract fun addEventListener(fragment: AbstractCommonFragment<R>, eventFun: UIEventHandler): Any?

    abstract fun removeListener(fragment: AbstractCommonFragment<R>, eventListener: Any?)

}