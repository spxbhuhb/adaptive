package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat

@Adat
class Color(
    val value: UInt,
    val opacity : Float = 1f
) {

    constructor(value: String) : this(value.parse())

    val hex by lazy { "#${value.toString(16).padStart(6, '0')}${(opacity * 255).toInt().toString(16).padStart(2, '0')}" }

    fun opaque(opacity: Float) = Color(value, opacity)

    /**
     * @return [value] in "#ffffff" format
     */
    @Deprecated("use hex instread", replaceWith = ReplaceWith("hex"))
    fun toHexColor(): String =
        hex

    companion object {
        fun String.parse(): UInt {
            val cleanedValue = this.trim().removePrefix("#")
            return when (cleanedValue.length) {
                3 -> cleanedValue.flatMap { listOf(it, it) }.joinToString("").toUInt(16)
                6 -> cleanedValue.toUInt(16)
                else -> throw IllegalArgumentException("Invalid color string format")
            }
        }
    }

}