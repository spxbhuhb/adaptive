/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import kotlin.math.max

abstract class AbstractBox<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {

        val data = renderData
        val container = renderData.container

        // ----  calculate height and width proposed to items  ------------------------

        val proposedItemWidth = data.layout?.instructedWidth ?: proposedWidth
        val proposedItemHeight = data.layout?.instructedHeight ?: proposedHeight

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
    }

}