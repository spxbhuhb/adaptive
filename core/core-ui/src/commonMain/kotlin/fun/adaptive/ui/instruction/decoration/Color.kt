package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat
import kotlin.math.roundToInt

@Adat
class Color(
    val value: UInt,
    val opacity: Double = 1.0
) {
    
    val hex by lazy { encodeToCssHex() }
    val rgba by lazy { encodeToCssRgba() }

    fun opaque(opacity: Double) = Color(value, opacity)

    fun encodeToCssRgba() =
        "rgba(${(value shr 16) and 0xFFu}, ${(value shr 8) and 0xFFu}, ${value and 0xFFu}, $opacity)"

    fun encodeToCssHex() =
        "#${value.toString(16).padStart(6, '0')}${(opacity * 255).toInt().toString(16).padStart(2, '0')}"

    fun encodeToShortHex() =
        "${value.toString(16).padStart(6, '0')}${(opacity * 255).toInt().let { if (it == 255) "" else it.toString(16).padStart(2, '0') }}"

    fun brightness(): Double {
        val r = ((value shr 16) and 0xFFu).toInt()
        val g = ((value shr 8) and 0xFFu).toInt()
        val b = (value and 0xFFu).toInt()

        return 0.299 * r + 0.587 * g + 0.114 * b
    }

    /**
     * Convert this color's RGB value into HSL (hue, saturation, lightness).
     */
    fun encodeToHsl(): HslColor {
        val r = ((value shr 16) and 0xFFu).toInt()
        val g = ((value shr 8) and 0xFFu).toInt()
        val b = (value and 0xFFu).toInt()

        val rf = r / 255.0
        val gf = g / 255.0
        val bf = b / 255.0

        val max = maxOf(rf, gf, bf)
        val min = minOf(rf, gf, bf)
        val delta = max - min

        val lightness = (max + min) / 2.0

        val saturation = if (delta == 0.0) {
            0.0
        } else {
            delta / (1.0 - kotlin.math.abs(2.0 * lightness - 1.0))
        }

        val hue = when {
            delta == 0.0 -> 0.0
            max == rf -> ((gf - bf) / delta) % 6.0
            max == gf -> ((bf - rf) / delta) + 2.0
            else -> ((rf - gf) / delta) + 4.0
        } * 60.0

        val normalizedHue = if (hue < 0.0) hue + 360.0 else hue

        return HslColor(
            normalizedHue,
            saturation.coerceIn(0.0, 1.0),
            lightness.coerceIn(0.0, 1.0),
            opacity
        )
    }

    companion object {

        fun decodeFromHex(text: String): Color =
            decodeFromHexOrNull(text) ?: throw IllegalArgumentException("Invalid color string format: $text")

        fun decodeFromHexOrNull(text: String): Color? {
            val cleanedValue = text.trim().removePrefix("#")

            return when (cleanedValue.length) {
                3 -> cleanedValue.flatMap { listOf(it, it) }.joinToString("").toUIntOrNull(16)?.let { Color(it) }
                6 -> cleanedValue.toUIntOrNull(16)?.let { Color(it) }
                8 -> {
                    val cv = cleanedValue.substring(0, 6).toUIntOrNull(16) ?: return null
                    val ov = cleanedValue.substring(6).toIntOrNull(16) ?: return null
                    Color(cv, ov.toDouble() / 255.0)
                }
                9 -> if (text == "undefined") Color(0x0u) else null
                else -> null
            }
        }

        fun decodeFromHsl(hue: Double, saturation: Double, lightness: Double, opacity: Double = 1.0): Color {
            val c = (1 - kotlin.math.abs(2 * lightness - 1)) * saturation
            val hPrime = hue / 60
            val x = c * (1 - kotlin.math.abs(hPrime % 2 - 1))
            val (r1, g1, b1) = when {
                hPrime < 1 -> Triple(c, x, 0.0)
                hPrime < 2 -> Triple(x, c, 0.0)
                hPrime < 3 -> Triple(0.0, c, x)
                hPrime < 4 -> Triple(0.0, x, c)
                hPrime < 5 -> Triple(x, 0.0, c)
                else -> Triple(c, 0.0, x)
            }
            val m = lightness - c / 2
            val r = ((r1 + m) * 255).roundToInt().coerceIn(0, 255)
            val g = ((g1 + m) * 255).roundToInt().coerceIn(0, 255)
            val b = ((b1 + m) * 255).roundToInt().coerceIn(0, 255)

            val rgb = (r shl 16) or (g shl 8) or b
            return Color(rgb.toUInt(), opacity)
        }
    }

}