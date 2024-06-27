/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.Alignment
import kotlin.math.max

abstract class AbstractColumn<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractStack<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun itemsWidthCalc(itemsWidth: Double, item: AbstractCommonFragment<RT>): Double =
        max(itemsWidth, item.renderData.finalWidth)

    override fun itemsHeightCalc(itemsHeight: Double, item: AbstractCommonFragment<RT>): Double =
        itemsHeight + item.renderData.finalHeight

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

    override fun AbstractCommonFragment<RT>.place(crossAxisAlignment: Alignment?, crossAxisSize: Double, offset: Double): Double {

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