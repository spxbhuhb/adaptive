/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import kotlin.math.max

abstract class AbstractBox<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize : Int = 2
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, stateSize
) {

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {

        val data = renderData
        val container = renderData.container

        val instructedWidth = renderData.layout?.instructedWidth
        val instructedHeight = renderData.layout?.instructedHeight

        // ----  calculate height and width proposed to items  ------------------------

        var proposedItemWidth = if (renderData.container?.horizontalScroll == true) {
            Double.POSITIVE_INFINITY
        } else {
            (instructedWidth ?: proposedWidth) - data.surroundingHorizontal
        }

        var proposedItemHeight = if (renderData.container?.verticalScroll == true) {
            Double.POSITIVE_INFINITY
        } else {
            (instructedHeight ?: proposedHeight) - data.surroundingVertical
        }

        // ----  calculate layout of all items  ---------------------------------------

        var itemsWidth = 0.0
        var itemsHeight = 0.0

        for (item in layoutItems) {
            item.computeLayout(proposedItemWidth, proposedItemHeight)
            itemsWidth = max(item.renderData.finalWidth, itemsWidth)
            itemsHeight = max(item.renderData.finalHeight, itemsHeight)
        }

        // ----  calculate sizes of this fragment  ------------------------------------

        computeFinal(proposedWidth, itemsWidth, proposedHeight, itemsHeight)

        val innerWidth = data.innerWidth !!
        val innerHeight = data.innerHeight !!

        // ----  place the items  -----------------------------------------------------

        val horizontalAlignment = container?.horizontalAlignment
        val verticalAlignment = container?.verticalAlignment

        for (item in layoutItems) {
            val itemLayout = item.renderData.layout

            val innerTop = itemLayout?.instructedTop ?: alignOnAxis(item.verticalAlignment, verticalAlignment, innerHeight, item.renderData.finalHeight)
            val innerLeft = itemLayout?.instructedLeft ?: alignOnAxis(item.horizontalAlignment, horizontalAlignment, innerWidth, item.renderData.finalWidth)

            item.placeLayout(innerTop + data.surroundingTop, innerLeft + data.surroundingStart)
        }

        placeStructural()
    }

    override fun updateLayout(updateId: Long, item: AbstractAuiFragment<*>?) {
        if (item == null) {
            super.updateLayout(updateId, item)
        } else {
            updateItemLayout(updateId, item)
        }
    }

    private fun updateItemLayout(updateId: Long, item: AbstractAuiFragment<*>) {
        val data = this.renderData
        val container = data.container

        val innerWidth = data.innerWidth ?: 0.0
        val innerHeight = data.innerHeight ?: 0.0

        item.computeLayout(innerWidth, innerHeight)

        val horizontalAlignment = container?.horizontalAlignment
        val verticalAlignment = container?.verticalAlignment

        val itemLayout = item.renderData.layout

        val innerTop = itemLayout?.instructedTop ?: alignOnAxis(item.verticalAlignment, verticalAlignment, innerHeight, item.renderData.finalHeight)
        val innerLeft = itemLayout?.instructedLeft ?: alignOnAxis(item.horizontalAlignment, horizontalAlignment, innerWidth, item.renderData.finalWidth)

        item.placeLayout(innerTop + data.surroundingTop, innerLeft + data.surroundingStart)

        item.updateBatchId = updateId
    }

}