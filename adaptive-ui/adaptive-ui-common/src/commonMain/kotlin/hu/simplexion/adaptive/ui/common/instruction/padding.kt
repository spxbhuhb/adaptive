/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

fun padding(all: DPixel) = Padding(all)
fun paddingTop(top: DPixel) = Padding(top = top)
fun paddingRight(right: DPixel) = Padding(right = right)
fun paddingBottom(bottom: DPixel) = Padding(bottom = bottom)
fun paddingLeft(left: DPixel) = Padding(left = left)

data class Padding(
    val top: DPixel = DPixel.NaP,
    val right: DPixel = DPixel.NaP,
    val bottom: DPixel = DPixel.NaP,
    val left: DPixel = DPixel.NaP
) : AdaptiveInstruction {

    constructor(all: DPixel) : this(all, all, all, all)

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { renderData ->
            renderData.padding = this or renderData.padding
        }
    }

    infix fun or(other: Padding) =
        Padding(
            this.top or other.top,
            this.right or other.right,
            this.bottom or other.bottom,
            this.left or other.left
        )

    companion object {
        val ZERO = Padding()
    }
}