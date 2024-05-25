/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.utility.alsoIfInstance

// ---- Shorthands --------------------------------------------------------

val Number.gridCol
    inline get() = GridCol(this.toInt())

val Number.gridRow
    inline get() = GridRow(this.toInt())

val Number.rowSpan
    inline get() = RowSpan(this.toInt())

val Number.colSpan
    inline get() = ColSpan(this.toInt())

// ---- Instructions --------------------------------------------------------

class RowSpan(val span : Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.rowSpan = span }
    }
}

class ColSpan(val span : Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.colSpan = span }
    }
}

class GridPos(val row : Int, val col : Int, val rowSpan : Int = 1, val colSpan: Int = 1) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> {
            it.gridRow = row
            it.gridCol = col
            it.rowSpan = rowSpan
            it.colSpan = colSpan
        }
    }

}

class GridRow(val row : Int, val span : Int = 1) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> {
            it.gridRow = row
            it.rowSpan = span
        }
    }

}

class GridCol(val col : Int, val span: Int = 1) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> {
            it.gridCol = col
            it.colSpan = span
        }
    }

}

class ColTemplate(vararg val tracks : Track) : AdaptiveInstruction

class RowTemplate(vararg val tracks : Track) : AdaptiveInstruction

// ---- Track --------------------------------------------------------

/**
 * IMPORTANT Tracks must be are immutable (or [Repeat] won't work).
 */
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