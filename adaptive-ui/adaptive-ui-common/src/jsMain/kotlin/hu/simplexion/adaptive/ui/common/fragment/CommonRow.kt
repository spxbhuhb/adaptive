/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.platform.align
import hu.simplexion.adaptive.ui.common.support.AbstractRow
import hu.simplexion.adaptive.ui.common.support.RawFrame
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
open class CommonRow(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractRow<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex, true) {

    override fun layout(proposedFrame: RawFrame?) {
        super.layout(proposedFrame)

        with(receiver.style) {
            display = "grid"
            renderData.container?.gapWidth?.let {
                if (! it.isNaN()) setProperty("column-gap", "${it}px")
            }
            setProperty("grid-auto-flow", "column")
            setProperty("grid-auto-columns", "min-content")
            setProperty("grid-template-rows", "1fr")
        }

        align()
        uiAdapter.applyLayoutToActual(this)
    }

}