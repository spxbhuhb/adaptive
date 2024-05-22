/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.browser.adapter.HTMLLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.fragment.distribute
import hu.simplexion.adaptive.ui.common.fragment.expand
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.ui.common.instruction.ColumnTemplate
import hu.simplexion.adaptive.ui.common.instruction.RowTemplate
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

open class AdaptiveGrid(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : HTMLLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun layout() {
        val frame = frame ?: BoundingRect(0f, 0f, 100f, 100f)

        val colTemp = checkNotNull(instructions.firstOrNullIfInstance<ColumnTemplate>()) { "missing column template in $this" }
        val rowTemp = checkNotNull(instructions.firstOrNullIfInstance<RowTemplate>()) { "missing row template in $this" }

        val columnSizes = distribute(frame.width, expand(colTemp.tracks))
        val rowSizes = distribute(frame.height, expand(rowTemp.tracks))

    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGrid"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGrid(parent.adapter, parent, index)

    }

}