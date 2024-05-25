/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.android.adapter.AndroidLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.Frame

open class AdaptiveBox(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AndroidLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun layout() {
        if (trace) trace("layout", "frame", frame)

//        for (item in items) {
//
//            val rect = item.fragment.renderInstructions.frame ?: DEFAULT_RECT
//
//            item.receiver.layout(
//                rect.x.toInt(),
//                rect.y.toInt(),
//                rect.width.toInt(),
//                rect.height.toInt()
//            )
//        }
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveBox"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveBox(parent.adapter as AdaptiveAndroidAdapter, parent, index)

    }

}