/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.common.fragment.layout

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.common.CommonAdapter
import `fun`.adaptive.ui.common.common
import `fun`.adaptive.ui.common.instruction.Alignment
import `fun`.adaptive.ui.common.instruction.SpaceDistribution
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
open class CommonColumn(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractColumn<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex) {

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        super.computeLayout(proposedWidth, proposedHeight)

//        with(receiver.style) {
//            display = "grid"
//            renderData.container?.gapHeight?.let {
//                if (! it.isNaN()) setProperty("row-gap", "${it}px")
//            }
//            setProperty("grid-auto-flow", "row")
//            setProperty("grid-auto-rows", "min-content")
//            setProperty("grid-template-columns", "1fr")
//        }

        //browserAlign()
    }

    fun browserAlign() {
        val style = receiver.style
        val container = renderData.container ?: return

        val distribution = container.spaceDistribution
        val horizontal = container.horizontalAlignment

        when {
            distribution == null && horizontal != null -> {
                when (horizontal) {
                    Alignment.Center -> style.setProperty("justify-items", "center")
                    Alignment.End -> style.setProperty("justify-items", "end")
                    Alignment.Start -> style.setProperty("justify-items", "start")
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
            Alignment.Center -> style.alignContent = "center"
            Alignment.End -> style.alignContent = "end"
            Alignment.Start -> style.alignContent = "start"
        }
    }
}