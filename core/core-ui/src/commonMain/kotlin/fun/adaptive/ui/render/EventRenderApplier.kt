package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.event.PointerEvents
import `fun`.adaptive.ui.instruction.event.UIEventHandler

abstract class EventRenderApplier<R> : AbstractRenderApplier() {

    fun applyTo(fragment: AbstractAuiFragment<R>) {
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

        if (previous?.pointerEvents != current?.pointerEvents) {
            applyNoPointerEvents(
                fragment,
                previous?.pointerEvents,
                current?.pointerEvents
            )
        }

        if (previous?.additionalEvents == false && current?.additionalEvents == false) return

        applyEventHandler(
            fragment,
            previous?.onDoubleClick,
            previous?.onDoubleClickListener,
            current?.onDoubleClick
        ).also {
            if (current != null) current.onDoubleClickListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onMove,
            previous?.onMoveListener,
            current?.onMove
        ).also {
            if (current != null) current.onMoveListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onLeave,
            previous?.onLeaveListener,
            current?.onLeave
        ).also {
            if (current != null) current.onLeaveListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onPrimaryDown,
            previous?.onPrimaryDownListener,
            current?.onPrimaryDown
        ).also {
            if (current != null) current.onPrimaryDownListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onPrimaryUp,
            previous?.onPrimaryUpListener,
            current?.onPrimaryUp
        ).also {
            if (current != null) current.onPrimaryUpListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onSecondaryDown,
            previous?.onSecondaryDownListener,
            current?.onSecondaryDown
        ).also {
            if (current != null) current.onSecondaryDownListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onSecondaryUp,
            previous?.onSecondaryUpListener,
            current?.onSecondaryUp
        ).also {
            if (current != null) current.onSecondaryUpListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onDrop,
            previous?.onDropListener,
            current?.onDrop
        ).also {
            if (current != null) current.onDropListener = it
        }

        applyEventHandler(
            fragment,
            previous?.onKeyDown,
            previous?.onKeyDownListener,
            current?.onKeyDown
        ).also {
            if (current != null) current.onKeyDownListener = it
        }

    }

    fun applyEventHandler(
        fragment: AbstractAuiFragment<R>,
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

    abstract fun applyNoPointerEvents(fragment: AbstractAuiFragment<R>, previous: PointerEvents?, current: PointerEvents?)

    abstract fun addEventListener(fragment: AbstractAuiFragment<R>, eventHandler: UIEventHandler): Any?

    abstract fun removeListener(fragment: AbstractAuiFragment<R>, eventListener: Any?)

}