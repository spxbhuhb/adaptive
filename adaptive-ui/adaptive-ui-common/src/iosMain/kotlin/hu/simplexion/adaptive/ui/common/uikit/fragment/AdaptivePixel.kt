/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.uikit.adapter.IOSLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake

open class AdaptivePixel(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : IOSLayoutFragment(adapter, parent, declarationIndex, 0, 2) {


    @OptIn(ExperimentalForeignApi::class)
    override fun layout() {
        if (trace) trace("layout", "frame", frame)

        for (item in items) {

            val rect = item.fragment.uiInstructions.frame ?: DEFAULT_RECT

            item.receiver.setFrame(
                CGRectMake(rect.x.toDouble(), rect.y.toDouble(), rect.width.toDouble(), rect.height.toDouble()),
            )
        }
    }

    companion object : AdaptiveFragmentCompanion {

        val DEFAULT_RECT = BoundingRect(100f, 100f, 100f, 100f)

        override val fragmentType = "$commonUI:AdaptivePixel"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptivePixel(parent.adapter, parent, index)

    }

}