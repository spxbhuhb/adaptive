package `fun`.adaptive.ui.input.color

import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.popup.PopupTheme

class ColorInputViewBackend(
    value: Color? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<Color, ColorInputViewBackend>(
    value, label, isSecret
), PopupSourceViewBackend  {

    var colorInputTheme = ColorInputTheme.default
    var popupTheme = PopupTheme.default

    fun safeHex() =
        inputValue?.encodeToShortHex() ?: ""

    fun fromString(input : String?) {
        if (input == null || input.length < 6) return
        val color = Color.decodeFromHexOrNull(input) ?: return
        inputValue = color
    }
}