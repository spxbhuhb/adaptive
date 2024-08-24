/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.ui.AbstractAuiAdapter

/**
 * IMPORTANT Tracks must be immutable (or [GridRepeat] won't work).
 */
interface GridTrack {

    val isIntrinsic: Boolean
        get() = true

    val isFix: Boolean
        get() = false

    val isFraction: Boolean
        get() = false

    val isMinContent: Boolean
        get() = false

    val value: Double

    fun expand(out: MutableList<GridTrack>) {
        out.add(this)
    }

    fun toRawValue(adapter: AbstractAuiAdapter<*, *>): Double

}