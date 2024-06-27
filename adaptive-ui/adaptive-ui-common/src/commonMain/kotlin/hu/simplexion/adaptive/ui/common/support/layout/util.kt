/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.Alignment

fun AbstractContainer<*, *>.computeFinal(proposedWidth: Double, itemsWidth: Double, proposedHeight: Double, itemsHeight: Double) {
    val data = renderData
    val layout = renderData.layout

    val innerWidth = when {
        layout?.instructedWidth != null -> layout.instructedWidth !! - data.surroundingHorizontal
        proposedWidth.isFinite() -> proposedWidth - data.surroundingHorizontal
        else -> itemsWidth
    }

    val innerHeight = when {
        layout?.instructedHeight != null -> layout.instructedHeight !! - data.surroundingVertical
        proposedHeight.isFinite() -> proposedHeight - data.surroundingVertical
        else -> itemsHeight
    }

    data.innerWidth = innerWidth
    data.innerHeight = innerHeight

    data.finalWidth = innerWidth + data.surroundingHorizontal
    data.finalHeight = innerHeight + data.surroundingVertical
}

val AbstractCommonFragment<*>.horizontalAlignment: Alignment?
    get() = renderData.layout?.horizontalAlignment

val AbstractCommonFragment<*>.verticalAlignment: Alignment?
    get() = renderData.layout?.verticalAlignment

fun alignOnAxis(alignSelf: Alignment?, alignItems: Alignment?, innerSize: Double, itemSize: Double) =
    when (alignSelf) {
        Alignment.Center -> (innerSize - itemSize) / 2
        Alignment.Start -> 0.0
        Alignment.End -> innerSize - itemSize
        else -> when (alignItems) {
            Alignment.Center -> (innerSize - itemSize) / 2
            Alignment.Start -> 0.0
            Alignment.End -> innerSize - itemSize
            null -> 0.0
        }
    }