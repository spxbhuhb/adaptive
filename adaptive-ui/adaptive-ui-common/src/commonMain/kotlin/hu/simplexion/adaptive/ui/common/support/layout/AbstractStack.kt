/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.Alignment
import hu.simplexion.adaptive.ui.common.instruction.SpaceDistribution

/**
 * Base class for stack which do not wrap: row, column.
 *
 * I've decided to go with this abstract function approach to have one algorithm that is direction
 * independent. I'm not 100% sure, but I hope the performance impact is negligible and this way
 * fixing bugs in the algorithm itself carries automatically to the descendant classes.
 *
 * Other way would be to pass a "horizontal" flag, but that would make [computeLayout] much harder
 * to read because of the many `if (horizontal)` branching.
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

    abstract fun itemsWidthCalc(itemsWidth: Double, item: AbstractCommonFragment<RT>): Double

    abstract fun itemsHeightCalc(itemsHeight: Double, item: AbstractCommonFragment<RT>): Double

    abstract fun instructedGap(): Double

    abstract fun freeSpace(innerWidth: Double, itemsWidth: Double, innerHeight: Double, itemsHeight: Double): Double

    abstract fun startOffset(): Double

    abstract fun mainAxisAlignment(): Alignment?

    abstract fun crossAxisAlignment(): Alignment?

    abstract fun crossAxisSize(innerWidth: Double, innerHeight: Double): Double

    abstract fun AbstractCommonFragment<RT>.place(crossAxisAlignment: Alignment?, crossAxisSize: Double, offset: Double): Double

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {

        val data = renderData
        val container = renderData.container

        val instructedGap = instructedGap()
        val itemCount = layoutItems.size

        // ----  calculate layout of all items  ---------------------------------------

        val totalGap = instructedGap() * (itemCount - 1)
        var itemsWidth = totalGap
        var itemsHeight = totalGap

        val proposedItemWidth = proposedWidth - data.surroundingHorizontal
        val proposedItemHeight = proposedHeight - data.surroundingVertical

        for (item in layoutItems) {
            item.computeLayout(proposedItemWidth, proposedItemHeight)
            itemsWidth = itemsWidthCalc(itemsWidth, item)
            itemsHeight = itemsHeightCalc(itemsHeight, item)
        }

        // ----  calculate sizes of this fragment  ------------------------------------

        computeFinal(proposedWidth, itemsWidth, proposedHeight, itemsHeight)

        val innerWidth = data.innerWidth !!
        val innerHeight = data.innerHeight !!

        // ---- calculate starting offset and gap based on instructions  --------------

        val freeSpace = freeSpace(innerWidth, itemsWidth, innerHeight, itemsHeight)

        val gap: Double
        var offset = startOffset()

        when (container?.spaceDistribution) {

            SpaceDistribution.Around -> {
                gap = freeSpace / (itemCount + 1)
                offset += gap
            }

            SpaceDistribution.Between -> {
                gap = freeSpace / (itemCount - 1)
            }

            else -> when (mainAxisAlignment()) {
                Alignment.Start -> {
                    gap = instructedGap
                }

                Alignment.Center -> {
                    gap = instructedGap
                    offset += freeSpace / 2.0
                }

                Alignment.End -> {
                    gap = instructedGap
                    offset += freeSpace
                }

                null -> {
                    gap = instructedGap
                }
            }
        }

        // ----  place the items  -----------------------------------------------------

        val crossAxisAlignment = crossAxisAlignment()
        val crossAxisSize = crossAxisSize(innerWidth, innerHeight)

        for (item in layoutItems) {
            offset += gap + item.place(crossAxisAlignment, crossAxisSize, offset)
        }

        for (item in structuralItems) {
            item.placeLayout(0.0, 0.0)
        }
    }

}