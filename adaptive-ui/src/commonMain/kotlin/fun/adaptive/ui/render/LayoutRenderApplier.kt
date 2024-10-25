package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawBorder
import `fun`.adaptive.ui.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.decoration.BackgroundGradient
import `fun`.adaptive.ui.instruction.decoration.Color

abstract class LayoutRenderApplier<R> : AbstractRenderApplier() {

    fun applyTo(fragment: AbstractAuiFragment<R>) {
        val previousData = fragment.previousRenderData
        val currentData = fragment.renderData

        val previous = previousData.layout
        val current = currentData.layout

        if (previous == current) return

        val receiver = fragment.receiver

        // ----  padding  ----

        val padding = current?.padding
        val previousPadding = previous?.padding

        if (padding != previousPadding) {
            applyPadding(receiver, padding)
        }

        // ----  margin  ----

        val margin = current?.margin
        val previousMargin = previous?.margin

        if (margin != previousMargin) {
            applyMargin(receiver, margin)
        }

        // ----  zIndex  ----

        val zIndex = current?.zIndex
        val previousZIndex = previous?.zIndex

        if (zIndex != previousZIndex) {
            applyZIndex(receiver, zIndex)
        }

        // ----  fixed  ----

        val fixed = current?.fixed
        val previousFixed = previous?.fixed

        if (fixed != previousFixed) {
            applyFixed(receiver, fixed)
        }
    }

    abstract fun applyPadding(receiver: R, padding: RawSurrounding?)

    abstract fun applyMargin(receiver: R, margin: RawSurrounding?)

    abstract fun applyZIndex(receiver: R, zIndex: Int?)

    abstract fun applyFixed(receiver: R, fixed: Boolean?)
}