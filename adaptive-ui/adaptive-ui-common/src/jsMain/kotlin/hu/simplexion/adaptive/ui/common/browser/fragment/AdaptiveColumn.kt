/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.adapter.HTMLLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.AlignContent
import hu.simplexion.adaptive.ui.common.instruction.AlignItems
import hu.simplexion.adaptive.ui.common.instruction.JustifyContent
import hu.simplexion.adaptive.ui.common.instruction.JustifyItems

open class AdaptiveColumn(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : HTMLLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun layout() {
        super.layout()
        with (receiver.style) {
            display = "flex"
            flexDirection = "column"
        }
        align()
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveColumn"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveColumn(parent.adapter, parent, index)

    }

}