/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

data class BorderRadius(val radius: Float) : AdaptiveUIInstruction {

    constructor(radius: Int) : this(radius.toFloat())
    constructor(radius: Double) : this(radius.toFloat())

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.borderRadius = radius
    }
}

data class BackgroundGradient(val degree: Float, val start: Color, val end: Color) : AdaptiveUIInstruction {

    constructor(degree: Int, start : Color, end : Color) : this(degree.toFloat(), start, end)
    constructor(degree: Double, start : Color, end : Color) : this(degree.toFloat(), start, end)

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.backgroundGradient = this
    }
}
