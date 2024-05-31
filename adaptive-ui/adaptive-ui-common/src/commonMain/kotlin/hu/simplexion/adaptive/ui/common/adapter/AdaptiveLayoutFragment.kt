/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.adapter

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction

/**
 * This is an interface instead of a class because we can't have two super classes in Kotlin.
 * This way each adapter may define its own UI fragment type and extend that UI fragment
 * type in the layout fragments.
 */
interface AdaptiveLayoutFragment<T : AdaptiveUIFragment> {

    val items : List<T>

    val instructions : Array<out AdaptiveInstruction>

    val renderData : RenderData

    val trace : Boolean

    fun trace(point : String, data : Any?)

}