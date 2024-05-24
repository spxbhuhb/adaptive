/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction


enum class Horizontal : AdaptiveUIInstruction {
    Start,
    Center,
    End;

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.horizontal = this
    }
}

enum class Vertical : AdaptiveUIInstruction {
    Start,
    Center,
    End;

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.justifySelf = this
    }
}

class BoundingRect(
    val x : Float,
    val y : Float,
    val width : Float,
    val height : Float
) : AdaptiveUIInstruction {

    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.frame = this
    }

    override fun toString(): String = "BoundingRect(x=$x, y=$y, width=$width, height=$height)"

    companion object {
        val DEFAULT = BoundingRect(100f, 100f, 100f, 100f)
    }
}

val Number.gridCol
    inline get() = GridCol(this.toInt())

val Number.gridRow
    inline get() = GridRow(this.toInt())

val Number.rowSpan
    inline get() = RowSpan(this.toInt())

val Number.colSpan
    inline get() = ColSpan(this.toInt())

class RowSpan(val span : Int) : AdaptiveUIInstruction {
    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.rowSpan = span
    }
}

class ColSpan(val span : Int) : AdaptiveUIInstruction {
    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.colSpan = span
    }
}

class GridPos(val row : Int, val col : Int, val rowSpan : Int = 1, val colSpan: Int = 1) : AdaptiveUIInstruction {
    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.gridRow = row
        uiInstructions.gridCol = col
        uiInstructions.rowSpan = rowSpan
        uiInstructions.colSpan = colSpan
    }
}

class GridRow(val row : Int, val span : Int = 1) : AdaptiveUIInstruction {
    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.gridRow = row
        uiInstructions.rowSpan = span
    }
}

class GridCol(val col : Int, val span: Int = 1) : AdaptiveUIInstruction {
    override fun apply(uiInstructions: UIInstructions) {
        uiInstructions.gridCol = col
        uiInstructions.colSpan = span
    }
}

class ColTemplate(vararg val tracks : Track) : AdaptiveInstruction

class RowTemplate(vararg val tracks : Track) : AdaptiveInstruction

interface Track {

    val isIntrinsic : Boolean
        get() = true

    val isFix : Boolean

    val value : Float

    fun expand(out : MutableList<Track>) {
        out.add(this)
    }

}

/**
 * Repeat [track] [count] times.
 *
 * IMPORTANT Assumes that the tracks are immutable.
 */
class Repeat(val count : Int, val track : Track) : Track {

    override val isIntrinsic : Boolean
        get() = false

    override val isFix: Boolean
        get() = throw UnsupportedOperationException()

    override val value: Float
        get() = throw UnsupportedOperationException()

    override fun expand(out : MutableList<Track>) {
        for (i in 0 until count) {
            if (track.isIntrinsic) {
                out.add(track)
            } else {
                track.expand(out)
            }
        }
    }

}