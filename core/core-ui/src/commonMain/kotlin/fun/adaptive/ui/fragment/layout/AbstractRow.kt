/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.Alignment
import kotlin.math.max

abstract class AbstractRow<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractStack<RT, CRT>(
    adapter, parent, declarationIndex, 2
) {

    override fun itemsWidthCalc(itemsWidth: Double, item: AbstractAuiFragment<RT>): Double =
        itemsWidth + item.renderData.finalWidth

    override fun itemsHeightCalc(itemsHeight: Double, item: AbstractAuiFragment<RT>): Double =
        max(itemsHeight, item.renderData.finalHeight)

    override fun constrainWidthCalc(remainingWidth: Double, item: AbstractAuiFragment<RT>, gap : Double): Double =
        remainingWidth - item.renderData.finalWidth - gap

    override fun constrainHeightCalc(remainingHeight: Double, item: AbstractAuiFragment<RT>, gap : Double): Double =
        remainingHeight

    override fun instructedGap(): Double =
        renderData.container?.gapWidth ?: 0.0

    override fun freeSpace(innerWidth: Double, itemsWidth: Double, innerHeight: Double, itemsHeight: Double): Double =
        innerWidth - itemsWidth

    override fun startOffset(): Double =
        renderData.surroundingStart

    override fun mainAxisAlignment(): Alignment? =
        renderData.container?.horizontalAlignment

    override fun crossAxisAlignment(): Alignment? =
        renderData.container?.verticalAlignment

    override fun crossAxisSize(innerWidth: Double, innerHeight: Double): Double =
        innerHeight

    override fun needsResizeToMax(innerWidth: Double, innerHeight: Double, proposedWidth: Double, proposedHeight: Double): Boolean {
        return proposedHeight.isInfinite() || innerHeight > proposedHeight
    }

    override fun resizeToMax(innerWidth: Double, innerHeight: Double, proposedWidth: Double, proposedHeight: Double, items: List<AbstractAuiFragment<RT>>) {
        for (item in items) {
            if (item.renderData.finalHeight < innerHeight) {
                item.computeLayout(proposedWidth, innerHeight)
            }
        }
    }

    override fun AbstractAuiFragment<RT>.place(crossAxisAlignment: Alignment?, crossAxisSize: Double, offset: Double): Double {

        val innerTop = alignOnAxis(
            verticalAlignment,
            crossAxisAlignment,
            crossAxisSize,
            renderData.finalHeight
        )

        placeLayout(innerTop + this@AbstractRow.renderData.surroundingTop, offset)

        return renderData.finalWidth
    }

}