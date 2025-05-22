package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.ui.instruction.decoration.Color.Companion.decodeFromHsl

class HslColor(
    val hue: Double,
    val saturation: Double,
    val lightness: Double,
    val opacity: Double
) {
    fun toColor() =
        decodeFromHsl(hue, saturation, lightness, opacity)
}