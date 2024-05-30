/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.uikit.adapter.IOSLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import kotlinx.cinterop.ExperimentalForeignApi

open class AdaptiveBox(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : IOSLayoutFragment(adapter, parent, declarationIndex, 0, 2) {


    @OptIn(ExperimentalForeignApi::class)
    override fun layout() {
        if (trace) trace("layout", "frame", frame)

        for (item in items) {

            val rect = item.fragment.renderInstructions.instructedPoint

//            item.receiver.setFrame(
//                CGRectMake(rect.left.toDouble(), rect.top.toDouble(), rect.width.toDouble(), rect.height.toDouble()),
//            )
        }
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveBox"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveBox(parent.adapter, parent, index)

    }

}