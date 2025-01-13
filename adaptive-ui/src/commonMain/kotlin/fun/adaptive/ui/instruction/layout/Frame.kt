/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.layout
import `fun`.adaptive.ui.render.model.LayoutRenderData

@Adat
class Frame(
    val top: DPixel,
    val left: DPixel,
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.instructedTop = adapter.toPx(top)
            it.instructedLeft = adapter.toPx(left)
            it.instructedWidth = adapter.toPx(width)
            it.instructedHeight = adapter.toPx(height)
        }
    }

    fun grow(dp: Double) = Frame(top - dp, left - dp, width + (2 * dp), height + (2 * dp))

    companion object {
        val NaF = Frame(DPixel.Companion.NaP, DPixel.Companion.NaP, DPixel.Companion.NaP, DPixel.Companion.NaP)
    }
}