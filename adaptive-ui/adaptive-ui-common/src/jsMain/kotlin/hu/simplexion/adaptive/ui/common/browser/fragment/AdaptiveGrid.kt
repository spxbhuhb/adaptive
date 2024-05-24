/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.adapter.HTMLLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.ColTemplate
import hu.simplexion.adaptive.ui.common.instruction.RowTemplate
import hu.simplexion.adaptive.ui.common.logic.distribute
import hu.simplexion.adaptive.ui.common.logic.expand
import hu.simplexion.adaptive.ui.common.logic.placeFragments
import hu.simplexion.adaptive.ui.common.logic.setFrame
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

open class AdaptiveGrid(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : HTMLLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun layout() {
        val colTemp = checkNotNull(instructions.firstOrNullIfInstance<ColTemplate>()) { "missing column template in $this" }
        val rowTemp = checkNotNull(instructions.firstOrNullIfInstance<RowTemplate>()) { "missing row template in $this" }

        val colOffsets = distribute(frame.width, expand(colTemp.tracks))
        val rowOffsets = distribute(frame.height, expand(rowTemp.tracks))

        placeFragments(items, colOffsets.size, rowOffsets.size)

        for (item in items) {
            setFrame(item, colOffsets, rowOffsets)
            item.setAbsolutePosition()
        }

    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGrid"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGrid(parent.adapter, parent, index)

    }

}