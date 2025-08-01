/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout.cellbox

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.ui.fragment.layout.alignOnAxis
import `fun`.adaptive.ui.fragment.layout.computeFinal
import `fun`.adaptive.ui.fragment.layout.horizontalAlignment
import `fun`.adaptive.ui.fragment.layout.verticalAlignment
import kotlin.math.max

/**
 * A layout that positions items according to a [CellBoxArrangement].
 *
 * Similar to AbstractBox but uses CellBoxArrangement to determine the position of items.
 */
abstract class AbstractCellBox<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize: Int = 4
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, stateSize
) {

    companion object {
        const val CELLS = 1
        const val ARRANGEMENT = 2
        // const val CONTENT = 3
    }

    val cellsOrNull get() = get<List<CellDef>?>(CELLS)
    val arrangementOrNull get() = get<CellBoxArrangement?>(ARRANGEMENT)

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        traceLayoutCompute(proposal)

        val data = renderData
        val container = renderData.container

        val gapWidth = renderData.container?.gapWidth ?: 0.0
        val gapHeight = renderData.container?.gapHeight ?: 0.0

        // ---- calculate height and width available to items ------------------------

        val itemProposal = proposal.toItemProposal(uiAdapter, renderData)

        // ---- get a pre-calculated arrangement or create a new one

        val arrangement = getOrBuildArrangement(itemProposal, gapWidth)
        println(arrangement)

        // ---- calculate layout of all items ---------------------------------------

        var itemIndex = 0
        var totalHeight = 0.0

        for (group in arrangement.groups) {
            val proposal = SizingProposal(group.calculatedWidth, group.calculatedWidth, 0.0, unbound)
            var groupHeight = gapHeight * (group.cells.size - 1)

            repeat(group.cells.size) {
                if (itemIndex >= layoutItems.size) return@repeat

                val item = layoutItems[itemIndex]

                item.computeLayout(proposal)

                groupHeight += item.renderData.finalHeight
                itemIndex ++
            }

            totalHeight = max(totalHeight, groupHeight)
        }

        computeFinal(proposal, arrangement.totalWidth, totalHeight)

        // ---- place the items -----------------------------------------------------

        val horizontalAlignment = container?.horizontalAlignment
        val verticalAlignment = container?.verticalAlignment

        var leftOffset = data.surroundingStart

        itemIndex = 0

        for (group in arrangement.groups) {
            var topOffset = data.surroundingTop

            repeat(group.cells.size) {
                val item = layoutItems[itemIndex]

                val id = item.renderData

                val innerTop = alignOnAxis(item.verticalAlignment, verticalAlignment, id.finalHeight, id.finalHeight)
                val innerLeft = alignOnAxis(item.horizontalAlignment, horizontalAlignment, group.calculatedWidth, id.finalWidth)

                item.placeLayout(topOffset + innerTop, leftOffset + innerLeft)

                topOffset += id.finalHeight + gapHeight

                itemIndex ++
            }

            leftOffset += group.calculatedWidth + gapWidth
        }

        placeStructural()
    }

    fun getOrBuildArrangement(
        itemProposal: SizingProposal,
        gapWidth: Double
    ): CellBoxArrangement {
        arrangementOrNull?.let { return it }

        val cells = checkNotNull(cellsOrNull) { "both arrangements and cells are null" }

        return CellBoxArrangementCalculator(uiAdapter).findBestArrangement(cells, itemProposal.containerWidth, gapWidth)
    }

    override fun updateLayout(updateId: Long, item: AbstractAuiFragment<*>?) {
        if (item == null) {
            super.updateLayout(updateId, item)
        } else {
            updateItemLayout(updateId, item)
        }
    }

    private fun updateItemLayout(updateId: Long, item: AbstractAuiFragment<*>) {
        // For a single item update, we'll recompute the entire layout
        // A more optimized approach would be to only update the position of the changed item
        // while maintaining the existing arrangement

        // FIXME updateItemLayout of AbstractCellBox is AI generated

        val data = this.renderData
        val innerWidth = data.innerWidth ?: 0.0
        val innerHeight = data.innerHeight ?: 0.0

        item.computeLayout(innerWidth, innerHeight)

        // Schedule a full layout update
        scheduleUpdate()

        item.updateBatchId = updateId
    }
}