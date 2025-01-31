/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.layout

@Adat
class Frame(
    val top: DPixel,
    val left: DPixel,
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {

    constructor(p1 : Position, p2 : Position) : this(
        DPixel.min(p1.top, p2.top),
        DPixel.min(p1.left, p2.left),
        DPixel.max(p1.left, p2.left) - DPixel.min(p1.left, p2.left),
        DPixel.max(p1.top, p2.top) - DPixel.min(p1.top, p2.top)
    )

    override fun applyTo(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.instructedTop = adapter.toPx(top)
            it.instructedLeft = adapter.toPx(left)
            it.instructedWidth = adapter.toPx(width)
            it.instructedHeight = adapter.toPx(height)
        }
    }

    operator fun contains(position: Position) : Boolean {
        if (this === NaF) return false
        val x = position.left.value
        val y = position.top.value
        val x1 = left.value
        val y1 = top.value
        val x2 = x1 + width.value
        val y2 = y1 + height.value
        return x >= x1 && x <= x2 && y >= y1 && y <= y2
    }

    fun grow(dp: Double) =
        if (this === NaF) {
            NaF
        } else {
            Frame(top - dp, left - dp, width + (2 * dp), height + (2 * dp))
        }

    companion object {
        val NaF = Frame(DPixel.Companion.NaP, DPixel.Companion.NaP, DPixel.Companion.NaP, DPixel.Companion.NaP)
    }
}