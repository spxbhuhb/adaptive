/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.resource.defaultResourceReader
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.utility.checkIfInstance
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIView
import platform.UIKit.UIViewContentMode

@AdaptiveActual(aui)
class AuiImage(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<UIView>(adapter, parent, index, 1, 2) {

    override val receiver = UIImageView()

    init {
        receiver.tag = id
    }

    private val content: DrawableResource
        get() = state[0].checkIfInstance()

    @OptIn(ExperimentalForeignApi::class)
    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            CoroutineScope(Dispatchers.IO).launch {
                val data = content.readAll()
                CoroutineScope(adapter.dispatcher).launch {
                    val nsData = data.usePinned { pinnedData ->
                        NSData.dataWithBytes(pinnedData.addressOf(0), data.size.toULong())
                    }
                    receiver.image = UIImage(data = nsData)
                    receiver.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
                }
            }
        }

        return false
    }

    @OptIn(ExperimentalForeignApi::class)
    class AUIImageView(
        val fragment: AuiImage
    ) : UIImageView(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

        override fun touchesEnded(touches: Set<*>, withEvent: platform.UIKit.UIEvent?) {
            fragment.instructions.firstOrNullIfInstance<OnClick>()
                ?.execute(UIEvent(fragment, withEvent))

            super.touchesBegan(touches, withEvent)
        }

    }

}