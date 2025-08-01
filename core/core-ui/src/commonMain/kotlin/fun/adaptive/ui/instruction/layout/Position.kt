/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.render.layout
import kotlin.math.max
import kotlin.math.min

@Adat
class Position(
    val top: DPixel,
    val left: DPixel
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.instructedTop = adapter.toPx(top)
            it.instructedLeft = adapter.toPx(left)
        }
    }

    fun toRaw(adapter: AdaptiveAdapter) : RawPosition {
        check(adapter is AbstractAuiAdapter<*,*>)

        return RawPosition(
            adapter.toPx(top),
            adapter.toPx(left)
        )
    }

    operator fun plus(value : Double) = Position(top + value, left + value)

    fun plus(top : DPixel, left : DPixel) = Position(this.top + top, this.left + left)

    fun plus(top : DPixel, left : DPixel, strategy: SizeStrategy) : Position {

        var topVal = this.top.value + top.value
        
        if (strategy.minWidth != null) {
            topVal = max(topVal, strategy.minWidth.value)
        }
        if (strategy.maxWidth != null) {
            topVal = min(topVal, strategy.maxWidth.value)
        }

        var leftVal = this.left.value + left.value

        if (strategy.minHeight != null) {
            leftVal = max(leftVal, strategy.minHeight.value)
        }
        if (strategy.maxHeight != null) {
            leftVal = min(leftVal, strategy.maxHeight.value)
        }

        return Position(DPixel(topVal), DPixel(leftVal))
    }

    companion object {
        val NaP = Position(DPixel.NaP, DPixel.NaP)
        val ZERO = Position(0.dp, 0.dp)
    }
}