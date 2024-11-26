/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.Alignment

fun AbstractContainer<*, *>.computeFinal(proposedWidth: Double, itemsWidth: Double, proposedHeight: Double, itemsHeight: Double) {
    val data = renderData
    val layout = renderData.layout

    val innerWidth = when {
        proposedWidth.isFinite() && layout?.fillHorizontal == true -> proposedWidth - data.surroundingHorizontal
        layout?.instructedWidth != null -> layout.instructedWidth !! - data.surroundingHorizontal
        else -> itemsWidth
    }

    val innerHeight = when {
        proposedHeight.isFinite() && layout?.fillVertical == true -> proposedHeight - data.surroundingVertical
        layout?.instructedHeight != null -> layout.instructedHeight !! - data.surroundingVertical
        else -> itemsHeight
    }

    data.innerWidth = innerWidth
    data.innerHeight = innerHeight

    data.finalWidth = innerWidth + data.surroundingHorizontal
    data.finalHeight = innerHeight + data.surroundingVertical
}

val AbstractAuiFragment<*>.horizontalAlignment: Alignment?
    get() = renderData.layout?.horizontalAlignment

val AbstractAuiFragment<*>.verticalAlignment: Alignment?
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