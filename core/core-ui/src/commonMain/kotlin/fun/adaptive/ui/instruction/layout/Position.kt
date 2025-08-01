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

    fun coerce(strategy: SizeStrategy) : Position {

        var leftVal = this.left.value

        if (strategy.minWidth != null) {
            leftVal = max(leftVal, strategy.minWidth.value)
        }
        if (strategy.maxWidth != null) {
            leftVal = min(leftVal, strategy.maxWidth.value)
        }

        var topVal = this.top.value

        if (strategy.minHeight != null) {
            topVal = max(topVal, strategy.minHeight.value)
        }
        if (strategy.maxHeight != null) {
            topVal = min(topVal, strategy.maxHeight.value)
        }

        return Position(DPixel(topVal), DPixel(leftVal))
    }

    companion object {
        val NaP = Position(DPixel.NaP, DPixel.NaP)
        val ZERO = Position(0.dp, 0.dp)
    }
}