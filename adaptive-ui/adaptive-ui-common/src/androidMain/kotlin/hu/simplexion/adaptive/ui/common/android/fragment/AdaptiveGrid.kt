/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.view.ViewGroup
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveViewGroup
import hu.simplexion.adaptive.ui.common.android.adapter.AndroidLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.ColTemplate
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.RowTemplate
import hu.simplexion.adaptive.ui.common.logic.distribute
import hu.simplexion.adaptive.ui.common.logic.expand
import hu.simplexion.adaptive.ui.common.logic.layoutGrid
import hu.simplexion.adaptive.ui.common.logic.placeFragments
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

open class AdaptiveGrid(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AndroidLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override val viewGroup: ViewGroup
        get() = receiver as ViewGroup

    override fun makeReceiver(): ViewGroup =
        AdaptiveViewGroup(androidAdapter.context, this)

    override fun layout(proposedFrame : Frame) {
        super.layout(proposedFrame)
        layoutGrid(items)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGrid"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGrid(parent.adapter as AdaptiveAndroidAdapter, parent, index)

    }

}