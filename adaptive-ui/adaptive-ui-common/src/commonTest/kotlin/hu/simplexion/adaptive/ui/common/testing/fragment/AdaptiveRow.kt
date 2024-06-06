/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.AbstractRow
import hu.simplexion.adaptive.ui.common.testing.AdaptiveUITestAdapter
import hu.simplexion.adaptive.ui.common.testing.TestReceiver

open class AdaptiveRow(
    adapter: AdaptiveUITestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractRow<TestReceiver, TestReceiver>(adapter, parent, declarationIndex, false) {

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveRow"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveRow(parent.adapter as AdaptiveUITestAdapter, parent, index)

    }

}