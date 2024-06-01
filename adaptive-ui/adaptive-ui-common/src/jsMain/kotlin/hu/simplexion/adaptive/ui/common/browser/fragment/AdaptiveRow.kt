/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.adapter.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.layout.AbstractRow
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class AdaptiveRow(
    adapter: AdaptiveBrowserAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractRow<HTMLDivElement, HTMLElement>(adapter, parent, declarationIndex, true) {

    override fun layout(proposedFrame: Frame) {
        super.layout(proposedFrame)
        receiver.style.display = "flex"
        align()
    }
    
    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveRow"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveRow(parent.adapter as AdaptiveBrowserAdapter, parent, index)

    }

}