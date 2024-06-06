/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.AbstractColumn
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class AdaptiveColumn(
    adapter: AdaptiveBrowserAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractColumn<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex, true) {

    override fun layout(proposedFrame: RawFrame) {
        super.layout(proposedFrame)
        with (receiver.style) {
            display = "flex"
            flexDirection = "column"
        }
        align()
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveColumn"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveColumn(parent.adapter as AdaptiveBrowserAdapter, parent, index)

    }

}