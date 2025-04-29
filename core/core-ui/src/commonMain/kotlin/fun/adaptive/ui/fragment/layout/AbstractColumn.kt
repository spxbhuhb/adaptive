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

    override fun constrainWidthCalc(remainingWidth: Double, item: AbstractAuiFragment<RT>, gap : Double): Double =
        remainingWidth

    override fun constrainHeightCalc(remainingHeight: Double, item: AbstractAuiFragment<RT>, gap : Double): Double =
        remainingHeight - item.renderData.finalHeight - gap

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

    override fun needsResizeToMax(itemsWidth: Double, itemsHeight: Double, proposedWidth: Double, proposedHeight: Double): Boolean {
        return proposedWidth.isInfinite() || itemsWidth > proposedWidth
    }

    override fun resizeToMax(itemsWidth: Double, itemsHeight: Double, proposedWidth: Double, proposedHeight: Double, items: List<AbstractAuiFragment<RT>>) {
        for (item in items) {
            if (item.renderData.finalWidth < itemsWidth) {
                item.computeLayout(itemsWidth, proposedHeight)
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