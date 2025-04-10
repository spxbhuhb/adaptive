/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.instruction.dp

/**
 * Repeat [track] [count] times.
 */
@Adat
class GridRepeat(
    val count: Int,
    val track: GridTrack = 0.dp
) : GridTrack {

    override val isExtend: Boolean
        get() = false

    override val value: Double
        get() = throw UnsupportedOperationException()

    override fun expand(out: MutableList<GridTrack>) {
        repeat(count) {
            out.add(track)
        }
    }

    override fun toRawValue(adapter: DensityIndependentAdapter): Double {
        throw UnsupportedOperationException()
    }

}