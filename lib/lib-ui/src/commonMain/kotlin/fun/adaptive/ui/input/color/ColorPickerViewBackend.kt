package `fun`.adaptive.ui.input.color

import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.decoration.Color

class ColorPickerViewBackend(
    value: Color? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<Color, ColorPickerViewBackend>(
    value, label, isSecret
) {

    var colorInputTheme = ColorInputTheme.default

    fun safeHex() =
        inputValue?.encodeToShortHex() ?: ""

    fun fromString(input : String?) {
        if (input == null || input.length < 6) return
        val color = Color.decodeFromHexOrNull(input) ?: return
        inputValue = color
    }
}