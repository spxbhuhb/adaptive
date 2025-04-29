/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.instruction.layout.SizeBase
import `fun`.adaptive.ui.instruction.toPx
import kotlin.math.max
import kotlin.math.min

fun AbstractAuiFragment<*>.computeFinal(proposedWidth: Double, itemsWidth: Double, proposedHeight: Double, itemsHeight: Double) {
    val data = renderData
    val layout = renderData.layout

    val strategy = layout?.sizeStrategy

    val unconstrainedInnerWidth = when {
        proposedWidth.isFinite() && strategy?.horizontalBase == SizeBase.Container -> proposedWidth - data.surroundingHorizontal
        layout?.instructedWidth != null -> layout.instructedWidth !! - data.surroundingHorizontal
        else -> itemsWidth
    }

    val unconstrainedInnerHeight = when {
        proposedHeight.isFinite() && strategy?.verticalBase == SizeBase.Container -> proposedHeight - data.surroundingVertical
        layout?.instructedHeight != null -> layout.instructedHeight !! - data.surroundingVertical
        else -> itemsHeight
    }

    val innerWidth = strategy?.let {
        min(
            max(
                it.minWidth?.toPx(uiAdapter) ?: 0.0,
                unconstrainedInnerWidth
            ),
            it.maxWidth?.toPx(uiAdapter) ?: Double.POSITIVE_INFINITY
        )
    } ?: unconstrainedInnerWidth

    val innerHeight = strategy?.let {
        min(
            max(
                it.minHeight?.toPx(uiAdapter) ?: 0.0,
                unconstrainedInnerHeight
            ),
            it.maxHeight?.toPx(uiAdapter) ?: Double.POSITIVE_INFINITY
        )
    } ?: unconstrainedInnerHeight

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