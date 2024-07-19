/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.instruction.OnClick
import hu.simplexion.adaptive.ui.common.instruction.UIEvent
import hu.simplexion.adaptive.ui.common.render.applyText
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UILabel
import platform.UIKit.UIView

@AdaptiveActual(common)
class CommonText(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<UIView>(adapter, parent, index, 1, 2) {

    override val receiver = AUILabel(this)

    init {
        receiver.tag = id
    }

    val content: String get() = state[0]?.toString() ?: ""

    @OptIn(ExperimentalForeignApi::class)
    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {

            // we have to apply instructions if they haven't changed
            // instructions sets the actual text fields of the UILabel
            // we don't have to do this if the instructions has been just applied
            // TODO think about layout effects of text changes

            if (! haveToPatch(dirtyMask, 1 shl instructionIndex)) {
                applyText(this)
            }

            receiver.intrinsicContentSize.useContents {
                renderData.innerWidth = this.width
                renderData.innerHeight = this.height
            }
        }

        return false
    }


    @OptIn(ExperimentalForeignApi::class)
    class AUILabel(
        val fragment: CommonText
    ) : UILabel(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

        override fun touchesEnded(touches: Set<*>, withEvent: platform.UIKit.UIEvent?) {
            fragment.instructions.firstOrNullIfInstance<OnClick>()
                ?.execute(UIEvent(fragment, withEvent))
            super.touchesBegan(touches, withEvent)
        }

    }
}