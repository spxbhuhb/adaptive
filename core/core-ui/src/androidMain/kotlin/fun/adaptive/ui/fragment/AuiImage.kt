/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AdaptiveActual(aui)
class AuiImage(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<View>(adapter, parent, index, stateSize()) {

    override val receiver = ImageView(adapter.context)

    private val content: GraphicsResourceSet
        by stateVariable()

    override fun auiPatchInternal() {

        if ( ! haveToPatch(content)) return

        CoroutineScope(Dispatchers.IO).launch {
            val data = content.readAll()
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            CoroutineScope(adapter.dispatcher).launch {
                receiver.setScaleType(ImageView.ScaleType.CENTER_CROP)
                receiver.setImageBitmap(bitmap)
            }
        }
    }

}