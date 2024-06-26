/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

abstract class AbstractRow<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractStack<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {

        val data = renderData
        val layout = renderData.layout
        val container = renderData.container

        for (item in layoutItems) {
            item.computeLayout(unbound, proposedHeight)
        }

        val innerWidth = when {
            layout?.instructedWidth != null -> layout.instructedWidth !!
            proposedWidth.isFinite() -> proposedWidth
            else -> layoutItems.sumOf { it.renderData.outerWidth } + (layoutItems.size - 1) * (container?.gapWidth ?: 0.0)
        }

        val innerHeight = when {
            layout?.instructedHeight != null -> layout.instructedHeight !!
            proposedHeight.isFinite() -> proposedHeight
            else -> layoutItems.maxOf { it.renderData.outerHeight }
        }

        data.innerWidth = innerWidth
        data.innerHeight = innerHeight

        data.outerWidth = innerWidth + data.surroundingHorizontal
        data.outerHeight = innerHeight + data.surroundingVertical

        if (trace) trace("compute-layout", "width: ${data.outerWidth}, height: ${data.outerHeight}")

        placeItems(horizontal = true)

    }

    override fun placeLayout(top: Double, left: Double) {
        TODO()
    }


}