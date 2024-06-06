/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.AbstractBox
import hu.simplexion.adaptive.ui.common.testing.AdaptiveUITestAdapter
import hu.simplexion.adaptive.ui.common.testing.TestReceiver

class AdaptiveBox(
    adapter: AdaptiveUITestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractBox<TestReceiver, TestReceiver>(adapter, parent, declarationIndex) {

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveBox"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveBox(parent.adapter as AdaptiveUITestAdapter, parent, index)

    }

}