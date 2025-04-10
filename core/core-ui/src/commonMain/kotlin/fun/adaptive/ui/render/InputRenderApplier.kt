package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawBorder
import `fun`.adaptive.ui.instruction.event.UIEventHandler

abstract class InputRenderApplier<R> : AbstractRenderApplier() {

    fun applyTo(fragment: AbstractAuiFragment<R>) {
        val previousData = fragment.previousRenderData
        val currentData = fragment.renderData

        val previous = previousData.input
        val current = currentData.input

        if (previous == current) return

        val receiver = fragment.receiver

        // tab index

        val tabIndex = current?.tabIndex
        val previousTabIndex = previous?.tabIndex

        if (tabIndex != previousTabIndex) {
            applyTabIndex(receiver, tabIndex)
        }
    }

    abstract fun applyTabIndex(receiver: R, tabIndex: Int?)


}