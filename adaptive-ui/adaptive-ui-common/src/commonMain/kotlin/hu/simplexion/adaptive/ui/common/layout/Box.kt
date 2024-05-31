/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Point
import hu.simplexion.adaptive.ui.common.instruction.Size

/**
 * Common box layout implementation for `box` fragments.
 */
class Box(
    val fragment : AdaptiveUIFragment,
) {

    fun measure(items : List<AdaptiveUIFragment>) : Size {
        fragment.traceMeasure()

        var width = 0f
        var height = 0f

        for (item in items) {
            val size = item.measure() ?: continue
            val point = item.renderData.instructedPoint ?: Point.ORIGIN

            width = maxOf(width, point.left + size.width)
            height = maxOf(height, point.top + size.height)
        }

        return Size(width, height)
    }

    fun layout(items : List<AdaptiveUIFragment>) {
        val proposedFrame = fragment.renderData.layoutFrame

        for (item in items) {
            item.layout(proposedFrame)
        }
    }

}

