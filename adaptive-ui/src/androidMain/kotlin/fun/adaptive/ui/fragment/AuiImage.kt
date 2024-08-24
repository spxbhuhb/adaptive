/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.resource.defaultResourceReader
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.utility.checkIfInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AdaptiveActual(aui)
class AuiImage(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<View>(adapter, parent, index, 1, 2) {

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

}