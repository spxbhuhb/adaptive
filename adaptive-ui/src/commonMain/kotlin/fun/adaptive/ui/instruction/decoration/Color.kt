package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat

@Adat
class Color(
    val value: UInt
) {

    constructor(value: String) : this(value.parse())

    /**
     * @return [value] in "#ffffff" format
     */
    fun toHexColor(): String =
        "#${value.toString(16).padStart(6, '0')}"

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