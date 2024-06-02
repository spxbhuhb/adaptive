/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.ui.common.testing.adapter.AdaptiveUITestAdapter
import hu.simplexion.adaptive.ui.common.testing.adapter.TestReceiver
import hu.simplexion.adaptive.utility.checkIfInstance

open class AdaptiveImage(
    adapter: AdaptiveUITestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<TestReceiver>(adapter, parent, index, 1, 2) {

    override val receiver = TestReceiver()

    private val res: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            res.uri
        }

        patchInstructions(closureMask)

        return false
    }

    /**
     * Measured size of images is unknown at the time measure is called, mostly because
     * the image is loaded asynchronously.
     *
     * Also, it is rare to use the actual size of the image for the layout, it is far
     * more usual to have a space and scale the image to fit that space.
     */
    override fun measure(): RawSize = instructedOr { RawSize.ZERO }

    override fun layout(proposedFrame: RawFrame) {
        super.calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveImage"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveImage(parent.adapter as AdaptiveUITestAdapter, parent, index)

    }

}