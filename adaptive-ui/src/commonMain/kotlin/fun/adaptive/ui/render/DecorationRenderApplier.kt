package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawBorder
import `fun`.adaptive.ui.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.instruction.decoration.BackgroundGradient
import `fun`.adaptive.ui.instruction.decoration.Color

abstract class DecorationRenderApplier<R> : AbstractRenderApplier() {

    fun applyTo(fragment: AbstractAuiFragment<R>) {
        val previousData = fragment.previousRenderData
        val currentData = fragment.renderData

        val previous = previousData.decoration
        val current = currentData.decoration

        if (previous == current) return

        val receiver = fragment.receiver

        // ----  border  ----

        val border = current?.border
        val previousBorder = previous?.border

        if (border != previousBorder) {
            applyBorder(receiver, border)
        }

        // ----  corner radius  ----

        val cornerRadius = current?.cornerRadius
        val previousCornerRadius = previous?.cornerRadius

        if (cornerRadius != previousCornerRadius) {
            applyCornerRadius(receiver, cornerRadius)
        }

        // ----  background  ----

        val previousColor = previous?.backgroundColor
        val previousGradient = previous?.backgroundGradient

        var clearBackground = (previousColor != null && previousGradient != null)

        val color = current?.backgroundColor

        if (color != null) {
            if (color != previousColor) {
                applyColor(receiver, color)
            }
            clearBackground = false
        } else {
            clearBackground = (previousColor != null)
        }

        val gradient = current?.backgroundGradient

        if (gradient != null) {
            if (previousGradient != gradient) {
                applyGradient(receiver, gradient, cornerRadius)
            }
            clearBackground = false
        } else {
            clearBackground = clearBackground || (previousGradient != null)
        }

        if (clearBackground) {
            clearBackground(receiver)
        }

        // drop shadow

        val dropShadow = current?.dropShadow
        val previousDropShadow = previous?.dropShadow

        if (dropShadow != previousDropShadow) {
            applyDropShadow(receiver, dropShadow)
        }
    }

    abstract fun applyBorder(receiver: R, border: RawBorder?)

    abstract fun applyCornerRadius(receiver: R, cornerRadius: RawCornerRadius?)

    abstract fun applyColor(receiver: R, color: Color)

    abstract fun applyGradient(receiver: R, gradient: BackgroundGradient, cornerRadius: RawCornerRadius?)

    abstract fun clearBackground(receiver: R)

    abstract fun applyDropShadow(receiver: R, dropShadow: RawDropShadow?)
}