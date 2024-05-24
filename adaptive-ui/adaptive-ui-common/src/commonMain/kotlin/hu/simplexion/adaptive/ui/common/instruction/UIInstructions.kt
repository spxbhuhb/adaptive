/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
 */
class UIInstructions(
    instructions : Array<out AdaptiveInstruction>
) {

    var frame : BoundingRect = BoundingRect.DEFAULT
    var color : Color? = null
    var minSize : Float? = null
    var maxSize : Float? = null
    var fontName: String? = null
    var fontSize: Float? = null
    var borderRadius: Float? = null
    var backgroundGradient : BackgroundGradient? = null
    var alignSelf : AlignSelf? = null
    var justifySelf: JustifySelf? = null
    var gridRow: Int? = null
    val gridCol: Int? = null
    val rowSpan: Int = 1
    val colSpan: Int = 1

    init {
        instructions.forEach{
            if (it is AdaptiveUIInstruction) it.apply(this)
        }
    }

    companion object {
        val DEFAULT = UIInstructions(emptyArray())
    }
}