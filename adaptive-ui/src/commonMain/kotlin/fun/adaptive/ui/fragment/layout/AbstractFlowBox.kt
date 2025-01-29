/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.FlowItemLimit
import kotlin.math.max

abstract class AbstractFlowBox<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, 2
) {

    class Row<RT>(
        val items: MutableList<AbstractAuiFragment<RT>> = mutableListOf(),
        var width: Double = 0.0,
        var height: Double = 0.0
    )

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {

        val data = renderData
        val container = renderData.container

        val itemLimit = instructions.firstInstanceOfOrNull<FlowItemLimit>()?.limit ?: Int.MAX_VALUE

        // ----  calculate layout of all items  ---------------------------------------

        val availableWidth = proposedWidth - data.surroundingHorizontal
        val colGap = container?.gapWidth ?: 0.0
        val rowGap = container?.gapHeight ?: 0.0

        val rows = mutableListOf<Row<RT>>(Row())
        var currentRow = rows.last()

        for (item in layoutItems) {
            item.computeLayout(unbound, unbound)

            val itemWidth = item.renderData.finalWidth
            val itemHeight = item.renderData.finalHeight

            if (
                (currentRow.width + itemWidth > availableWidth && currentRow.items.isNotEmpty())
                || (itemLimit <= currentRow.items.size)
            ) {
                currentRow.width -= colGap
                currentRow = Row()
                rows += currentRow
            }

            currentRow.items += item
            currentRow.width += itemWidth + colGap
            currentRow.height = max(currentRow.height, itemHeight)
        }

        if (currentRow.items.isNotEmpty()) {
            currentRow.width -= colGap
        }

        val itemsWidth = rows.maxOf { it.width }
        val itemsHeight = rows.sumOf { it.height } + rowGap * (rows.size - 1)

        // ----  calculate sizes of this fragment  ------------------------------------

        computeFinal(proposedWidth, itemsWidth, proposedHeight, itemsHeight)

        // ----  place the items  -----------------------------------------------------

        val horizontalAlignment = container?.horizontalAlignment
        val verticalAlignment = container?.verticalAlignment

        var topOffset = data.surroundingTop
        var leftOffset = data.surroundingStart

        for (row in rows) {
            for (item in row.items) {
                val id = item.renderData

                val innerTop = alignOnAxis(item.verticalAlignment, verticalAlignment, row.height, id.finalHeight)
                val innerLeft = alignOnAxis(item.horizontalAlignment, horizontalAlignment, row.width, id.finalWidth)

                item.placeLayout(topOffset + innerTop, leftOffset + innerLeft)

                leftOffset += id.finalWidth + colGap
            }

            topOffset += row.height + rowGap
            leftOffset = data.surroundingStart
        }

        placeStructural()
    }

}