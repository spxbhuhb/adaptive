/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

class Color(val value: Int) : AdaptiveUIInstruction {

    /**
     * @return [value] in "#ffffff" format
     */
    fun toHexColor() : String =
        "#${value.toString(16).padStart(6, '0')}"

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.color = this
    }

}

val white = Color(0xffffff)
val black = Color(0x000000)

val red = Color(0xff0000)
val green = Color(0x00ff00)
val blue = Color(0x0000ff)