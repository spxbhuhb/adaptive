package `fun`.adaptive.ui.common.instruction

import `fun`.adaptive.adat.Adat

@Adat
class Color(
    val value: UInt
) {

    /**
     * @return [value] in "#ffffff" format
     */
    fun toHexColor(): String =
        "#${value.toString(16).padStart(6, '0')}"

}