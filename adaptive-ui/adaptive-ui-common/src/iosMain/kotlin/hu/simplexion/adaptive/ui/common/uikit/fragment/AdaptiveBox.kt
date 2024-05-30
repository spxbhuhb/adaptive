/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.uikit.adapter.IosLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.Frame
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

open class AdaptiveBox(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : IosLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun makeReceiver(): UIView = UIView()

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