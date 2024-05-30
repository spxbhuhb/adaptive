/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.adapter.BrowserLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.Point
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class AdaptiveBox(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : BrowserLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun layout(proposedFrame : Frame) {
        super.layout(proposedFrame)
        val boxFrame = renderInstructions.layoutFrame
        for (item in items) {
            item.layout(boxFrame)
        }
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveBox"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveBox(parent.adapter, parent, index)

    }

}