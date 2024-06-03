/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.resource.defaultResourceReader
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.ui.common.uikit.adapter.AdaptiveIosAdapter
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.*

class AdaptiveImage(
    adapter: AdaptiveIosAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<UIView>(adapter, parent, index, 1, 2) {

    override val receiver = UIImageView()

    private val content: DrawableResource
        get() = state[0].checkIfInstance()

    @OptIn(ExperimentalForeignApi::class)
    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        // this has to be done first, so text styled (font, size, etc.) are applied during measure
        if (haveToPatch(closureMask, 1 shl instructionIndex)) {
            uiAdapter.applyRenderInstructions(this)
        }

        if (haveToPatch(closureMask, 1)) {
            // FIXME start image read in a different thread and during mount maybe?
            CoroutineScope(adapter.dispatcher).launch {
                val data = defaultResourceReader.read(content.path)

                val nsData = data.usePinned { pinnedData ->
                    NSData.dataWithBytes(pinnedData.addressOf(0), data.size.toULong())
                }

                receiver.image = UIImage(data = nsData)
                receiver.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
            }
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
    override fun measure() = instructedOr { RawSize.ZERO }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveImage"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveImage(parent.adapter as AdaptiveIosAdapter, parent, index)

    }

}