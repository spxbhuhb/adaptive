/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.ui.common.testing.adapter.AdaptiveUITestAdapter
import hu.simplexion.adaptive.ui.common.testing.adapter.TestReceiver

open class AdaptiveText(
    adapter: AdaptiveUITestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<TestReceiver>(adapter, parent, index, 1, 2) {

    override val receiver = TestReceiver()

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            content
        }

        if (haveToPatch(closureMask, 1 shl instructionIndex)) {
            renderData = RenderData(instructions)
            uiAdapter.applyRenderInstructions(this)
        }

        return false
    }


    /**
     * In web browsers measuring text is not the usual way.
     */
    override fun measure(): RawSize = instructedOr { RawSize(content.length * 20f, 20f) }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveText"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveText(parent.adapter as AdaptiveUITestAdapter, parent, index)

    }

}