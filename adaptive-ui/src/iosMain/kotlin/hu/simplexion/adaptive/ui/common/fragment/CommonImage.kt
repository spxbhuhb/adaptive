/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.resource.defaultResourceReader
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.instruction.OnClick
import hu.simplexion.adaptive.ui.common.instruction.UIEvent
import hu.simplexion.adaptive.utility.checkIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
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

@AdaptiveActual(common)
class CommonImage(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<UIView>(adapter, parent, index, 1, 2) {

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

    @OptIn(ExperimentalForeignApi::class)
    class AUIImageView(
        val fragment: CommonImage
    ) : UIImageView(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

        override fun touchesEnded(touches: Set<*>, withEvent: platform.UIKit.UIEvent?) {
            fragment.instructions.firstOrNullIfInstance<OnClick>()
                ?.execute(UIEvent(fragment, withEvent))

            super.touchesBegan(touches, withEvent)
        }

    }

}