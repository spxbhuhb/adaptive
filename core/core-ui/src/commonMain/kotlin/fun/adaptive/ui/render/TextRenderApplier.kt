package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.render.model.TextRenderData

abstract class TextRenderApplier<R> : AbstractRenderApplier() {

    inline fun <T> call(current : T, previous : T, call : (T) -> Unit) {
        if (current != previous) call(current)
    }

    fun applyTo(fragment: AbstractAuiFragment<R>) {
        val previousData = fragment.previousRenderData
        val currentData = fragment.renderData

        val previous = previousData.text
        val current = currentData.text

        if (previous == current) return

        applyTo(fragment.receiver, previous, current, fragment.uiAdapter.defaultTextRenderData)
    }

    fun applyTo(receiver: R, previous: TextRenderData?, current: TextRenderData?, default: TextRenderData) {
        call(current?.fontName, previous?.fontName) { applyFontName(receiver, it ?: default.fontName) }
        call(current?.fontSize, previous?.fontSize) { applyFontSize(receiver, it ?: default.fontSize) }
        call(current?.fontWeight, previous?.fontWeight) { applyFontWeight(receiver, it ?: default.fontWeight) }
        call(current?.letterSpacing, previous?.letterSpacing) { applyLetterSpacing(receiver, it) }
        call(current?.wrap, previous?.wrap) { applyWrap(receiver, it) }
        call(current?.color, previous?.color) { applyColor(receiver, it) }
        call(current?.smallCaps, previous?.smallCaps) { applySmallCaps(receiver, it) }
        call(current?.noSelect, previous?.noSelect) { applyNoSelect(receiver, it) }
    }

    abstract fun applyFontName(receiver: R, fontName: String?)

    abstract fun applyFontSize(receiver: R, fontSize: SPixel?)

    abstract fun applyFontWeight(receiver: R, fontWeight: Int?)

    abstract fun applyLetterSpacing(receiver: R, letterSpacing: Double?)

    abstract fun applyWrap(receiver: R, wrap: Boolean?)

    abstract fun applyColor(receiver: R, color: Color?)

    abstract fun applySmallCaps(receiver: R, smallCaps: Boolean?)

    abstract fun applyNoSelect(receiver: R, noSelect: Boolean?)

}