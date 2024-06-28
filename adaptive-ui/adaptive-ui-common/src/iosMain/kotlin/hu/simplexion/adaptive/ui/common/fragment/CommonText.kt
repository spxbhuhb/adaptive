/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UILabel
import platform.UIKit.UIView

@AdaptiveActual(common)
class CommonText(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<UIView>(adapter, parent, index, 1, 2) {

    override val receiver = UILabel()

    private val content: String get() = state[0]?.toString() ?: ""

    @OptIn(ExperimentalForeignApi::class)
    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            receiver.text = content
            receiver.textAlignment = NSTextAlignmentCenter
            receiver.translatesAutoresizingMaskIntoConstraints = false
            receiver.intrinsicContentSize.useContents {
                renderData.innerWidth = this.width
                renderData.innerHeight = this.height
            }
        }

        return false
    }

}