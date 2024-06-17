/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.AlignItems
import hu.simplexion.adaptive.ui.common.instruction.AlignSelf
import hu.simplexion.adaptive.ui.common.instruction.JustifyContent

/**
 * Two uses: layouts and loop/select containers.
 *
 */
abstract class AbstractStackFragment<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AbstractContainerFragment<RT, CRT>(
    adapter, parent, declarationIndex, instructionsIndex, stateSize
) {

    fun measure(
        widthFun: (width: Double, itemLeft: Double, itemWidth: Double) -> Double,
        heightFun: (height: Double, itemTop: Double, itemHeight: Double) -> Double
    ) {
        var width = 0.0
        var height = 0.0

        for (item in layoutItems) {
            item.measure()

            val layout = item.renderData.layout

            width = widthFun(width, layout?.left ?: 0.0, item.renderData.boxWidth)
            height = heightFun(height, layout?.top ?: 0.0, item.renderData.boxHeight)
        }

        renderData.measuredWidth = width
        renderData.measuredHeight = height
    }

    fun calcPrefixAndGap(horizontal: Boolean, availableSpace: Double, prefixPadding: Double): Pair<Double, Double> {
        val gap = if (horizontal) {
            renderData.container?.gapWidth ?: 0.0
        } else {
            renderData.container?.gapHeight ?: 0.0
        }

        if (layoutItems.isEmpty()) return (0.0 to gap)

        val used = calcUsedSpace(horizontal)

        val gapCount = layoutItems.size - 1
        val usedByInstructedGap = gapCount * gap
        val remaining = availableSpace - (used + usedByInstructedGap)

        if (remaining <= 0) return (0.0 to gap)

        return when (renderData.container?.justifyContent) {
            JustifyContent.Start -> (prefixPadding + 0.0 to gap)
            JustifyContent.Center -> (prefixPadding + (remaining / 2) to gap)
            JustifyContent.End -> (prefixPadding + remaining to gap)
            null -> (prefixPadding + 0.0 to gap)
        }
    }

    fun calcUsedSpace(horizontal: Boolean): Double {

        var usedSpace = 0.0

        for (item in layoutItems) {
            if (horizontal) {
                usedSpace += item.renderData.boxWidth
            } else {
                usedSpace += item.renderData.boxHeight
            }
        }

        return usedSpace
    }

    fun calcAlign(alignItems: AlignItems?, alignSelf: AlignSelf?, availableSpace: Double, usedSpace: Double): Double =
        if (alignSelf != null) {
            when (alignSelf) {
                AlignSelf.Center -> (availableSpace - usedSpace) / 2
                AlignSelf.Start -> 0.0
                AlignSelf.End -> availableSpace - usedSpace
            }
        } else {
            when (alignItems) {
                AlignItems.Center -> (availableSpace - usedSpace) / 2
                AlignItems.Start -> 0.0
                AlignItems.End -> availableSpace - usedSpace
                null -> 0.0
            }
        }

    /**
     * Common layout func for row and column layouts.
     *
     * @param horizontal True the items should be next to each other (row), false if they should be below each other (column).
     */
    fun layoutStack(horizontal: Boolean, autoSizing: Boolean) {

        if (autoSizing) {
            for (item in layoutItems) {
                item.layout(null)
            }
            return
        }

        val padding = renderData.layout?.padding ?: RawSurrounding.ZERO

        val spaceForAlign = if (horizontal) {
            renderData.measuredWidth
        } else {
            renderData.measuredHeight
        }

        val (prefix, gap) = calcPrefixAndGap(horizontal, spaceForAlign, if (horizontal) padding.left else padding.top)

        var offset = prefix

        val alignItems = renderData.container?.alignItems

        for (item in layoutItems) {
            val renderData = item.renderData

            val alignSelf = renderData.layout?.alignSelf

            val frame = if (horizontal) {
                val top = calcAlign(alignItems, alignSelf, spaceForAlign, renderData.boxHeight)
                RawFrame(padding.top + top, offset, renderData.boxWidth, renderData.boxHeight)
            } else {
                val left = calcAlign(alignItems, alignSelf, spaceForAlign, renderData.boxWidth)
                RawFrame(offset, padding.left + left, renderData.boxWidth, renderData.boxHeight)
            }

            item.layout(frame)

            offset += gap + (if (horizontal) renderData.boxWidth else renderData.boxHeight)
        }

        for (item in structuralItems) {
            item.layout(RawFrame(0.0, 0.0, layoutFrame.width, layoutFrame.height))
        }
    }

}