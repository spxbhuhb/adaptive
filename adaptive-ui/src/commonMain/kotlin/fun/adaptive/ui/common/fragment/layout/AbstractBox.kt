/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.AbstractCommonFragment
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

        val proposedItemWidth = (data.layout?.instructedWidth ?: proposedWidth) - data.surroundingHorizontal
        val proposedItemHeight = (data.layout?.instructedHeight ?: proposedHeight) - data.surroundingVertical

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

    override fun layoutChange(fragment: AbstractCommonFragment<*>) {
        val data = this.renderData
        val container = data.container

        val innerWidth = data.innerWidth !!
        val innerHeight = data.innerHeight !!

        fragment.computeLayout(innerWidth, innerHeight)

        val horizontalAlignment = container?.horizontalAlignment
        val verticalAlignment = container?.verticalAlignment

        val itemLayout = fragment.renderData.layout

        val innerTop = itemLayout?.instructedTop ?: alignOnAxis(fragment.verticalAlignment, verticalAlignment, innerHeight, fragment.renderData.finalHeight)
        val innerLeft = itemLayout?.instructedLeft ?: alignOnAxis(fragment.horizontalAlignment, horizontalAlignment, innerWidth, fragment.renderData.finalWidth)

        fragment.placeLayout(innerTop + data.surroundingTop, innerLeft + data.surroundingStart)
    }

}