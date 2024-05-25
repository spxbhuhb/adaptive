/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.browser.adapter.HTMLLayoutFragment
import hu.simplexion.adaptive.ui.common.browser.adapter.LayoutItem
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.ColTemplate
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.RowTemplate
import hu.simplexion.adaptive.ui.common.logic.distribute
import hu.simplexion.adaptive.ui.common.logic.expand
import hu.simplexion.adaptive.ui.common.logic.placeFragments
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

open class AdaptiveGrid(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : HTMLLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun measure() {
        super.measure()

        val colTemp = checkNotNull(instructions.firstOrNullIfInstance<ColTemplate>()) { "missing column template in $this" }
        val rowTemp = checkNotNull(instructions.firstOrNullIfInstance<RowTemplate>()) { "missing row template in $this" }

        val colOffsets = distribute(renderInstructions.layoutFrame.width, expand(colTemp.tracks))
        val rowOffsets = distribute(renderInstructions.layoutFrame.height, expand(rowTemp.tracks))

        if (trace) {
            trace("measure-layoutFrame", renderInstructions.layoutFrame)
            trace("measure-colOffsets", colOffsets.contentToString())
            trace("measure-rowOffsets", rowOffsets.contentToString())
        }

        placeFragments(items, rowOffsets.size - 1, colOffsets.size - 1)

        for (item in items) {
            item.setFrame(colOffsets, rowOffsets)
        }
    }

    override fun layout() {
        super.layout()
        for (item in items) {
            item.layout()
        }
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGrid"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGrid(parent.adapter, parent, index)

    }

}