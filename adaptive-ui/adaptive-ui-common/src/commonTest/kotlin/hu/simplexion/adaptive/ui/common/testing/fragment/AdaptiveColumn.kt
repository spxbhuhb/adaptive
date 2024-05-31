/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.layout.AbstractColumn
import hu.simplexion.adaptive.ui.common.testing.adapter.AdaptiveUITestAdapter
import hu.simplexion.adaptive.ui.common.testing.adapter.TestReceiver

open class AdaptiveColumn(
    adapter: AdaptiveUITestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractColumn<TestReceiver, TestReceiver>(adapter, parent, declarationIndex) {

    override fun layout(proposedFrame: Frame) {
        super.layout(proposedFrame)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveColumn"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveColumn(parent.adapter as AdaptiveUITestAdapter, parent, index)

    }

}