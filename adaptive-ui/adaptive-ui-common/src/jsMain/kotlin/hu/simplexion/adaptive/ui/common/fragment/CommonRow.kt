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
import hu.simplexion.adaptive.ui.common.support.layout.AbstractRow
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
open class CommonRow(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractRow<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex) {

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        super.computeLayout(proposedWidth, proposedHeight)

//        with(receiver.style) {
//            display = "grid"
//            renderData.container?.gapWidth?.let {
//                if (! it.isNaN()) setProperty("column-gap", "${it}px")
//            }
//            setProperty("grid-auto-flow", "column")
//            setProperty("grid-auto-columns", "auto")
//            setProperty("grid-template-rows", "1fr")
//        }

        // browserAlign()
    }

    fun browserAlign() {
        val style = receiver.style
        val container = renderData.container ?: return

        val distribution = container.spaceDistribution
        val horizontal = container.horizontalAlignment

        when {
            distribution == null && horizontal != null -> {
                when (horizontal) {
                    Alignment.Center -> style.setProperty("justify-content", "center")
                    Alignment.End -> style.setProperty("justify-content", "end")
                    Alignment.Start -> style.setProperty("justify-content", "start")
                }
            }

            distribution == SpaceDistribution.Between -> {
                TODO()
            }

            distribution == SpaceDistribution.Around -> {
                TODO()
            }
        }

        when (container.verticalAlignment) {
            null -> Unit
            Alignment.Center -> style.alignItems = "center"
            Alignment.End -> style.alignItems = "end"
            Alignment.Start -> style.alignItems = "start"
        }
    }
}