package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.adat.Adat

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