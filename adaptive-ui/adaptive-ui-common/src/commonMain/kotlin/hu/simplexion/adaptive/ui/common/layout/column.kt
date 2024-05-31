/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Size
import kotlin.math.max

fun AdaptiveUIFragment.measureColumn(items : List<AdaptiveUIFragment>) : Size {
    traceMeasure()

    val instructedSize = renderData.instructedSize

    if (instructedSize != null) {
        for (item in items) {
            item.measure()
        }
        return instructedSize
    }

    var width = 0f
    var height = 0f

    for (item in items) {
        check(item.renderData.instructedPoint == null) { "column does not support absolute positioning" }

        val size = checkNotNull(item.measure()) { "unable to measure column, cannot get size of: $item" }

        width = max(width, size.width)
        height += size.height
    }

    return Size(width, height)
}

fun AdaptiveUIFragment.layoutColumn(items : List<AdaptiveUIFragment>) {
    val boxFrame = renderData.layoutFrame
    for (item in items) {
        item.layout(boxFrame)
    }
}