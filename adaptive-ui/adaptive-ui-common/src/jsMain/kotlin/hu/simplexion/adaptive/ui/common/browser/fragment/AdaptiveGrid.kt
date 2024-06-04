/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.AbstractGrid
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class AdaptiveGrid(
    adapter: AdaptiveBrowserAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractGrid<HTMLDivElement, HTMLElement>(adapter, parent, declarationIndex) {

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGrid"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGrid(parent.adapter as AdaptiveBrowserAdapter, parent, index)

    }

}