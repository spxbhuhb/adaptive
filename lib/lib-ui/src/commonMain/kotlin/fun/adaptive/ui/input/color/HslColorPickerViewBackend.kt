package `fun`.adaptive.ui.input.color

import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.decoration.Color.Companion.decodeFromHsl
import `fun`.adaptive.ui.instruction.decoration.HslColor

class HslColorPickerViewBackend(
    value: HslColor? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<HslColor, HslColorPickerViewBackend>(
    value, label, isSecret
) {

    var colorInputTheme = ColorInputTheme.default

    fun safeHex() =
        inputValue?.toColor()?.encodeToShortHex() ?: ""

    fun fromString(input : String?) {
        if (input == null || input.length < 6) return
        val color = Color.decodeFromHexOrNull(input)?.encodeToHsl() ?: return
        inputValue = color
    }
}