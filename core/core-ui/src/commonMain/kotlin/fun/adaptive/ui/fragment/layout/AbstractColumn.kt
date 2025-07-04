/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.Alignment
import kotlin.math.max

abstract class AbstractColumn<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractStack<RT, CRT>(
    adapter, parent, declarationIndex, 2
) {

    override fun itemsWidthCalc(itemsWidth: Double, item: AbstractAuiFragment<RT>): Double =
        max(itemsWidth, item.renderData.finalWidth)

    override fun itemsHeightCalc(itemsHeight: Double, item: AbstractAuiFragment<RT>): Double =
        itemsHeight + item.renderData.finalHeight

    override fun constrainCalc(itemProposal: SizingProposal, item: AbstractAuiFragment<RT>, gap: Double): SizingProposal {
        val decrement = item.renderData.finalHeight + gap
        return SizingProposal(
            itemProposal.minWidth,
            itemProposal.maxWidth,
            max(0.0, itemProposal.minHeight - decrement),
            max(0.0, itemProposal.maxHeight - decrement)
        )
    }

    override fun instructedGap(): Double =
        renderData.container?.gapHeight ?: 0.0

    override fun freeSpace(innerWidth: Double, itemsWidth: Double, innerHeight: Double, itemsHeight: Double): Double =
        innerHeight - itemsHeight

    override fun startOffset(): Double =
        renderData.surroundingTop

    override fun mainAxisAlignment(): Alignment? =
        renderData.container?.verticalAlignment

    override fun crossAxisAlignment(): Alignment? =
        renderData.container?.horizontalAlignment

    override fun crossAxisSize(innerWidth: Double, innerHeight: Double): Double =
        innerWidth

    override fun needsResizeToMax(innerWidth: Double, innerHeight: Double, proposal: SizingProposal): Boolean {
        return proposal.maxWidth.isInfinite() || proposal.maxWidth != proposal.minWidth
    }

    override fun resizeToMax(innerWidth: Double, innerHeight: Double, items: List<AbstractAuiFragment<RT>>) {
        for (item in items) {
            if (item.renderData.finalWidth < innerWidth) {
                item.computeLayout(innerWidth, item.renderData.finalHeight)
            }
        }
    }

    override fun AbstractAuiFragment<RT>.place(crossAxisAlignment: Alignment?, crossAxisSize: Double, offset: Double): Double {

        val innerLeft = alignOnAxis(
            horizontalAlignment,
            crossAxisAlignment,
            crossAxisSize,
            renderData.finalWidth
        )

        placeLayout(offset, innerLeft + this@AbstractColumn.renderData.surroundingStart)

        return renderData.finalHeight
    }

}