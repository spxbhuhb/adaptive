/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.resource.defaultResourceReader
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AdaptiveImage(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<View>(adapter, parent, index, 1, 2) {

    override val receiver = ImageView(adapter.context)

    private val content: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()
        if (closureMask == 0) return false

        if (haveToPatch(closureMask, 1)) {
            // FIXME start image read in a different thread and during mount maybe?
            CoroutineScope(adapter.dispatcher).launch {
                val data = defaultResourceReader.read(content.path)
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                receiver.setScaleType(ImageView.ScaleType.CENTER_CROP)
                receiver.setImageBitmap(bitmap)
            }
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
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveImage"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveImage(parent.adapter as AdaptiveAndroidAdapter, parent, index)

    }

}