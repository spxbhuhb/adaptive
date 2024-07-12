package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.layout.RawBorder
import hu.simplexion.adaptive.ui.common.fragment.layout.RawCornerRadius
import hu.simplexion.adaptive.ui.common.fragment.layout.RawDropShadow
import hu.simplexion.adaptive.ui.common.instruction.BackgroundGradient
import hu.simplexion.adaptive.ui.common.instruction.Color

abstract class DecorationRenderApplier<R> : AbstractRenderApplier() {

    fun applyTo(fragment: AbstractCommonFragment<R>) {
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
        }

        val gradient = current?.backgroundGradient

        if (gradient != null) {
            if (previousGradient != gradient) {
                applyGradient(receiver, gradient, cornerRadius)
            }
            clearBackground = false
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