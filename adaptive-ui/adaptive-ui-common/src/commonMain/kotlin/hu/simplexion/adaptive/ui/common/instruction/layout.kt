/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.ui.common.fragment.AdaptiveUIFragment

data class BoundingRect(
    val x : Float,
    val y : Float,
    val width : Float,
    val height : Float
) : AdaptiveUIInstruction {

    override fun apply(uiInstructions: AdaptiveUIFragment.UIInstructions) {
        uiInstructions.frame = this
    }

    override fun toString(): String = "BoundingRect(x=$x, y=$y, width=$width, height=$height)"

    companion object {
        val DEFAULT = BoundingRect(100f, 100f, 100f, 100f)
    }
}

class ColumnTemplate(
    vararg val tracks : Track
) {
    fun expand() {

    }
}

class RowTemplate(
    vararg val tracks : Track
)

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