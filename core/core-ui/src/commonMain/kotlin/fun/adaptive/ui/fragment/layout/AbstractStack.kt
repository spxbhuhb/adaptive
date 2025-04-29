/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.log.devInfo
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.api.fill
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.instruction.layout.SpaceDistribution

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
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize: Int
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, stateSize
) {

    abstract fun itemsWidthCalc(itemsWidth: Double, item: AbstractAuiFragment<RT>): Double

    abstract fun itemsHeightCalc(itemsHeight: Double, item: AbstractAuiFragment<RT>): Double

    abstract fun constrainWidthCalc(remainingWidth: Double, item: AbstractAuiFragment<RT>, gap : Double): Double

    abstract fun constrainHeightCalc(remainingHeight: Double, item: AbstractAuiFragment<RT>, gap : Double): Double

    abstract fun instructedGap(): Double

    abstract fun freeSpace(innerWidth: Double, itemsWidth: Double, innerHeight: Double, itemsHeight: Double): Double

    abstract fun startOffset(): Double

    abstract fun mainAxisAlignment(): Alignment?

    abstract fun crossAxisAlignment(): Alignment?

    abstract fun crossAxisSize(innerWidth: Double, innerHeight: Double): Double

    abstract fun AbstractAuiFragment<RT>.place(crossAxisAlignment: Alignment?, crossAxisSize: Double, offset: Double): Double

    abstract fun needResizeToMax(
        itemsWidth: Double,
        itemsHeight: Double,
        proposedWidth: Double,
        proposedHeight: Double
    ): Boolean

    abstract fun resizeToMax(
        itemsWidth: Double,
        itemsHeight: Double,
        proposedWidth: Double,
        proposedHeight: Double,
        items: List<AbstractAuiFragment<RT>>
    )

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {

        val data = renderData
        val container = renderData.container

        val instructedWidth = renderData.layout?.instructedWidth
        val instructedHeight = renderData.layout?.instructedHeight

        val instructedGap = instructedGap()
        val itemCount = layoutItems.size

        // ----  calculate layout of all items  ---------------------------------------

        val totalGap = instructedGap() * (itemCount - 1)
        var itemsWidth = totalGap
        var itemsHeight = totalGap

        val horizontalScroll = (container?.horizontalScroll == true)
        val verticalScroll = (container?.verticalScroll == true)

        var proposedItemWidth = if (horizontalScroll) {
            Double.POSITIVE_INFINITY
        } else {
            val w = (instructedWidth ?: proposedWidth) - data.surroundingHorizontal
            if (verticalScroll) w - uiAdapter.scrollBarSize else w
        }

        var proposedItemHeight = if (verticalScroll) {
            Double.POSITIVE_INFINITY
        } else {
            val h = (instructedHeight ?: proposedHeight) - data.surroundingVertical
            if (horizontalScroll) h - uiAdapter.scrollBarSize else h
        }

        val fillStrategy = data.layout?.fill ?: fill.none
        val items = if (fillStrategy.reverse) {
            layoutItems.reversed()
        } else {
            layoutItems
        }

        for (item in items) {
            item.computeLayout(proposedItemWidth, proposedItemHeight)
            itemsWidth = itemsWidthCalc(itemsWidth, item)
            itemsHeight = itemsHeightCalc(itemsHeight, item)
            if (fillStrategy.constrain) {
                proposedItemWidth = constrainWidthCalc(proposedItemWidth, item, instructedGap)
                proposedItemHeight = constrainHeightCalc(proposedItemHeight, item, instructedGap)
            }
        }

        // ----  resize items if requested sizes of this fragment  --------------------

        if (fillStrategy.resizeToMax && needResizeToMax(itemsWidth, itemsHeight, proposedWidth, proposedHeight)) {
            resizeToMax(itemsWidth, itemsHeight, proposedWidth, proposedHeight, items)
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

        placeStructural()
    }

}