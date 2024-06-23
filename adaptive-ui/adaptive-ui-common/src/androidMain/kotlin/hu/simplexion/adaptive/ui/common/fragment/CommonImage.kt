/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.resource.defaultResourceReader
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.support.layout.RawFrame
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AdaptiveActual(common)
class CommonImage(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<View>(adapter, parent, index, 1, 2) {

    override val receiver = ImageView(adapter.context)

    private val content: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            // FIXME start image read in a different thread and during mount maybe?
            CoroutineScope(adapter.dispatcher).launch {
                val data = defaultResourceReader.read(content.path)
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                receiver.setScaleType(ImageView.ScaleType.CENTER_CROP)
                receiver.setImageBitmap(bitmap)
            }
        }

        return false
    }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

}