/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.adapter.HTMLLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect

open class AdaptivePixel(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : HTMLLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun layout() {
        if (trace) trace("layout", "frame", frame)

        for (item in items) {

            val rect = item.fragment.uiInstructions.frame ?: DEFAULT_RECT

            item.receiver.style.let {
                it.position = "absolute"
                it.top = "${rect.y}px"
                it.left = "${rect.x}px"
                it.width = "${rect.width}px"
                it.height = "${rect.height}px"
            }
        }
    }

    companion object : AdaptiveFragmentCompanion {

        val DEFAULT_RECT = BoundingRect(100f, 100f, 100f, 100f)

        override val fragmentType = "$commonUI:AdaptivePixel"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptivePixel(parent.adapter, parent, index)

    }

}