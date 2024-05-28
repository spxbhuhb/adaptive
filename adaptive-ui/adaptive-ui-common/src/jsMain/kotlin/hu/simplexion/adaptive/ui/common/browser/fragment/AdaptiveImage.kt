/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.resource.DefaultResourceReader
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLImageElement

open class AdaptiveImage(
    adapter: AdaptiveAdapter,
    parent : AdaptiveFragment,
    index : Int
) : AdaptiveUIFragment(adapter, parent, index, 1, 2) {

    override val receiver = document.createElement("img") as HTMLImageElement

    private val res: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.src = res.uri
        }

        if (haveToPatch(closureMask, instructionIndex)) {
            applyRenderInstructions()
        }

        return false
    }

    override fun layout() {
        receiver.width = renderInstructions.layoutFrame.width.toInt()
        receiver.height = renderInstructions.layoutFrame.height.toInt()
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveImage"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveImage(parent.adapter, parent, index)

    }

}