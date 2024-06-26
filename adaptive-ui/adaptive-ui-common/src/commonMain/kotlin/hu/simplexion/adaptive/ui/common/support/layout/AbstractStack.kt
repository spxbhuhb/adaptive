/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.Alignment

/**
 * Base class for stack which do not wrap: box, row, column.
 */
abstract class AbstractStack<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, instructionsIndex, stateSize
) {

    /**
     * Common layout func for row and column layouts.
     *
     * @param horizontal True the items should be next to each other (row), false if they should be below each other (column).
     */
    fun placeItems(horizontal: Boolean) {
        val container = renderData.container

        if (uiAdapter.autoSizing) {
            for (item in layoutItems) {
                item.placeLayout(Double.NaN, Double.NaN)
            }
            return
        }

        val padding = renderData.layout?.padding ?: RawSurrounding.ZERO

        val mainAxisAvailableSpace = (if (horizontal) renderData.innerWidth else renderData.innerHeight) ?: 0.0 // FIXME stack layout main axis available

        val (prefix, gap) = calcPrefixAndGap(horizontal, mainAxisAvailableSpace, if (horizontal) padding.start else padding.top)

        var offset = prefix

        val crossAxisAvailableSpace = (if (horizontal) renderData.innerHeight else renderData.innerWidth) ?: 0.0 // FIXME stack layout cross axis available
        val crossAxisItemAlignment = if (horizontal) container?.verticalAlignment else container?.horizontalAlignment

        for (item in layoutItems) {
            val renderData = item.renderData
            val layout = renderData.layout
            val box = renderData.box

            val crossAxisSelfAlignment = (if (horizontal) layout?.verticalAlignment else layout?.horizontalAlignment) ?: Alignment.Start // FIXME cross axis alignment

            if (horizontal) {
                val top = calcAlign(crossAxisItemAlignment, crossAxisSelfAlignment, crossAxisAvailableSpace, box.height)
                item.placeLayout(padding.top + top, offset)
            } else {
                val left = calcAlign(crossAxisItemAlignment, crossAxisSelfAlignment, crossAxisAvailableSpace, box.width)
                item.placeLayout(offset, padding.start + left)
            }

            offset += gap + (if (horizontal) box.width else box.height)
        }

        for (item in structuralItems) {
            item.placeLayout(0.0, 0.0)
        }
    }

    fun calcPrefixAndGap(horizontal: Boolean, availableSpace: Double, prefixPadding: Double): Pair<Double, Double> {
        val container = renderData.container

        val gap = if (horizontal) {
            container?.gapWidth ?: 0.0
        } else {
            container?.gapHeight ?: 0.0
        }

        if (layoutItems.isEmpty()) return (0.0 to gap)

        val used = calcUsedSpace(horizontal)

        val gapCount = layoutItems.size - 1
        val usedByInstructedGap = gapCount * gap
        val remaining = availableSpace - (used + usedByInstructedGap)

        if (remaining <= 0) return (0.0 to gap)

        val alignment = if (horizontal) container?.horizontalAlignment else container?.verticalAlignment

        return when (alignment) {
            Alignment.Start -> (prefixPadding + 0.0 to gap)
            Alignment.Center -> (prefixPadding + (remaining / 2) to gap)
            Alignment.End -> (prefixPadding + remaining to gap)
            null -> (prefixPadding + 0.0 to gap)
        }
    }

    fun calcUsedSpace(horizontal: Boolean): Double {

        var usedSpace = 0.0

        for (item in layoutItems) {
            val box = item.renderData.box
            if (horizontal) {
                usedSpace += box.width
            } else {
                usedSpace += box.height
            }
        }

        return usedSpace
    }

    fun calcAlign(alignItems: Alignment?, alignSelf: Alignment?, availableSpace: Double, usedSpace: Double): Double =
        if (alignSelf != null) {
            when (alignSelf) {
                Alignment.Center -> (availableSpace - usedSpace) / 2
                Alignment.Start -> 0.0
                Alignment.End -> availableSpace - usedSpace
            }
        } else {
            when (alignItems) {
                Alignment.Center -> (availableSpace - usedSpace) / 2
                Alignment.Start -> 0.0
                Alignment.End -> availableSpace - usedSpace
                null -> 0.0
            }
        }



}