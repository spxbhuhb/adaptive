/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

class BorderRadius(val radius: Float) : AdaptiveUIInstruction {

    constructor(radius: Int) : this(radius.toFloat())

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.borderRadius = radius
    }
}

class Border(val color: Color, val width: Float = 1f) : AdaptiveUIInstruction {

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.border = this
    }
}


class BackgroundColor(val color: Color) : AdaptiveUIInstruction {

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.backgroundColor = this.color
    }

}


class BackgroundGradient(val degree: Float, val start: Color, val end: Color) : AdaptiveUIInstruction {

    constructor(degree: Int, start : Color, end : Color) : this(degree.toFloat(), start, end)

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.backgroundGradient = this
    }
}
