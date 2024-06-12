/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.layout.AbstractGrid
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
open class AdaptiveGrid(
    adapter: AdaptiveBrowserAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractGrid<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex) {

    override fun layout(proposedFrame: RawFrame) {
        super.layout(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

}