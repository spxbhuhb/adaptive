/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.instruction.Alignment
import hu.simplexion.adaptive.ui.common.instruction.SpaceDistribution
import hu.simplexion.adaptive.ui.common.support.AbstractColumn
import hu.simplexion.adaptive.ui.common.support.RawFrame
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
open class CommonColumn(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractColumn<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex, true) {

    override fun layout(proposedFrame: RawFrame?) {
        super.layout(proposedFrame)

        with(receiver.style) {
            display = "grid"
            renderData.container?.gapHeight?.let {
                if (! it.isNaN()) setProperty("row-gap", "${it}px")
            }
            setProperty("grid-template-columns", "1fr")
            setProperty("grid-auto-rows", "min-content")
        }

        align()
        uiAdapter.applyLayoutToActual(this)
    }

    fun align() {
        val style = receiver.style
        val container = renderData.container ?: return

        val distribution = container.spaceDistribution
        val vertical = container.verticalAlignment

        when {
            distribution == null && vertical != null -> {
                when (vertical) {
                    Alignment.Center -> style.setProperty("justify-items", "center")
                    Alignment.End -> style.setProperty("justify-items", "end")
                    Alignment.Start -> style.setProperty("justify-items", "start")
                }
            }

            distribution == SpaceDistribution.Between -> {
                style.setProperty("justify-items", "space-between")
            }

            distribution == SpaceDistribution.Around -> {
                style.setProperty("justify-items", "space-around")
            }
        }

        when (container.horizontalAlignment) {
            null -> Unit
            Alignment.Center -> style.alignItems = "center"
            Alignment.End -> style.alignItems = "end"
            Alignment.Start -> style.alignItems = "start"
        }
    }
}