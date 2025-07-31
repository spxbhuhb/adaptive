/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.ui.DensityIndependentAdapter

/**
 * IMPORTANT Tracks must be immutable (or [GridRepeat] won't work).
 */
interface GridTrack : AdatClass {

    val isFix: Boolean
        get() = false

    val isFraction: Boolean
        get() = false

    val value: Double

    val isExtend: Boolean

    fun expand(out: MutableList<GridTrack>) {
        out.add(this)
    }

    fun toRawValue(adapter: DensityIndependentAdapter): Double

}