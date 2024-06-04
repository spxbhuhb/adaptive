/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

open class AdaptiveImage(
    adapter: AdaptiveBrowserAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    override val receiver: HTMLImageElement =
        document.createElement("img") as HTMLImageElement

    private val res: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            receiver.src = res.uri
        }

        return false
    }

    /**
     * Measured size of images is unknown at the time measure is called, mostly because
     * the image is loaded asynchronously.
     *
     * Also, it is rare to use the actual size of the image for the layout, it is far
     * more usual to have a space and scale the image to fit that space.
     */
    override fun measure(): RawSize = instructedOr { RawSize.NaS }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)

        val size = layoutFrame.size

        receiver.width = size.width.toInt()
        receiver.height = size.height.toInt()

        uiAdapter.applyLayoutToActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveImage"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveImage(parent.adapter as AdaptiveBrowserAdapter, parent, index)

    }

}