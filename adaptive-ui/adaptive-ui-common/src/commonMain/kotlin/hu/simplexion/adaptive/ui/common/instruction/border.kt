/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.CommonRenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

fun borderRadius(all: DPixel) = BorderRadius(all)

fun borderTopRadius(top: DPixel) = BorderRadius(topLeft = top, topRight = top)
fun borderBottomRadius(bottom: DPixel) = BorderRadius(bottomLeft = bottom, bottomRight = bottom)

fun borderTopLeftRadius(topLeft: DPixel) = BorderRadius(topLeft = topLeft)
fun borderTopRightRadius(topRight: DPixel) = BorderRadius(topRight = topRight)
fun borderBottomLeftRadius(bottomLeft: DPixel) = BorderRadius(bottomLeft = bottomLeft)
fun borderBottomRightRadius(bottomRight: DPixel) = BorderRadius(bottomRight = bottomRight)

fun borderRadius(
    topLeft: DPixel = DPixel.NaP,
    topRight: DPixel = DPixel.NaP,
    bottomLeft: DPixel = DPixel.NaP,
    bottomRight: DPixel = DPixel.NaP
) = BorderRadius(topLeft, topRight, bottomLeft, bottomRight)

data class BorderRadius(
    val topLeft: DPixel = DPixel.NaP,
    val topRight: DPixel = DPixel.NaP,
    val bottomLeft: DPixel = DPixel.NaP,
    val bottomRight: DPixel = DPixel.NaP
) : AdaptiveInstruction {

    constructor(all: DPixel) : this(all, all, all, all)

    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.borderRadius = this }
    }

    infix fun or(other: BorderRadius) =
        BorderRadius(
            this.topLeft or other.topLeft,
            this.topRight or other.topRight,
            this.bottomLeft or other.bottomLeft,
            this.bottomRight or other.bottomRight
        )

    companion object {
        val ZERO = BorderRadius()
    }
}

fun border(color: Color, width: DPixel) = Border(color, width)

data class Border(val color: Color, val width: DPixel = 1.dp) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.border = this }
    }

    companion object {
        // FIXME make Border.NONE transparent
        val NONE = Border(Color(0x000000), 0.dp)
    }
}
