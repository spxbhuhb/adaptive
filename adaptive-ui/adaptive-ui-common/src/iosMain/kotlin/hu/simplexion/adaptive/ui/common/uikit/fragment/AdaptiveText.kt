/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.ui.common.uikit.AdaptiveIosAdapter
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UILabel
import platform.UIKit.UIView

@AdaptiveActual(common)
class AdaptiveText(
    adapter: AdaptiveIosAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<UIView>(adapter, parent, index, 1, 2) {

    override val receiver = UILabel()

    private val content: String get() = state[0]?.toString() ?: ""

    @OptIn(ExperimentalForeignApi::class)
    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            receiver.text = content
            receiver.textAlignment = NSTextAlignmentCenter
            receiver.translatesAutoresizingMaskIntoConstraints = false
            measuredSize = receiver.intrinsicContentSize.useContents {
                RawSize(this.width.toFloat(), this.height.toFloat())
            }
        }

        return false
    }

    override fun measure() = instructedOr { measuredSize }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

}