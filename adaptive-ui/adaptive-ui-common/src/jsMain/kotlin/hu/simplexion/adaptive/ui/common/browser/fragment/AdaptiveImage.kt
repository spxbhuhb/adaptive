/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.browser.adapter.BrowserUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

open class AdaptiveImage(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : BrowserUIFragment(adapter, parent, index, 1, 2) {

    val imgReceiver
        get() = receiver as HTMLImageElement

    private val res: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            imgReceiver.src = res.uri
        }

        if (haveToPatch(closureMask, 1 shl instructionIndex)) {
            renderData = RenderData(instructions)
            applyRenderInstructions()
        }

        return false
    }

    override fun makeReceiver(): HTMLElement =
        document.createElement("img") as HTMLElement

    /**
     * Measured size of images is unknown at the time measure is called, mostly because
     * the image is loaded asynchronously.
     *
     * Also, it is rare to use the actual size of the image for the layout, it is far
     * more usual to have a space and scale the image to fit that space.
     */
    override fun measure() = Unit

    override fun layout(proposedFrame: Frame) {
        super.layout(proposedFrame)

        val size = renderData.layoutFrame.size

        imgReceiver.width = size.width.toInt()
        imgReceiver.height = size.height.toInt()
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveImage"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveImage(parent.adapter, parent, index)

    }

}