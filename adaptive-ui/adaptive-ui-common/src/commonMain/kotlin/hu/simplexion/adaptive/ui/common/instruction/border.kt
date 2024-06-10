/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

fun borderRadius(radius: DPixel) = BorderRadius(radius)

data class BorderRadius(val radius: DPixel) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.borderRadius = radius }
    }
}

fun border(color: Color, width: DPixel) = Border(color, width)

data class Border(val color: Color, val width: DPixel = 1.dp) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.border = this }
    }

    companion object {
        // FIXME make Border.NONE transparent
        val NONE = Border(Color(0x000000), 0.dp)
    }
}
