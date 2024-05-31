/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.AbstractGrid
import hu.simplexion.adaptive.ui.common.testing.adapter.AdaptiveUITestAdapter
import hu.simplexion.adaptive.ui.common.testing.adapter.TestReceiver

open class AdaptiveGrid(
    adapter: AdaptiveUITestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractGrid<TestReceiver, TestReceiver>(adapter, parent, declarationIndex) {

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGrid"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGrid(parent.adapter as AdaptiveUITestAdapter, parent, index)

    }

}