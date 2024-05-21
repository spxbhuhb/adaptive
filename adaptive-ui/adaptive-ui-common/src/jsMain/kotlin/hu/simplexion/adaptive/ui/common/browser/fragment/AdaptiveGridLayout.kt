/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.browser.adapter.AdaptiveBrowserFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.fragment.distribute
import hu.simplexion.adaptive.ui.common.fragment.expand
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.ui.common.instruction.ColumnTemplate
import hu.simplexion.adaptive.ui.common.instruction.RowTemplate
import hu.simplexion.adaptive.ui.common.instruction.px
import hu.simplexion.adaptive.utility.applyIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.math.exp

open class AdaptiveGrid(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AdaptiveBrowserFragment(adapter, parent, declarationIndex, 0, 2) {

    override val receiver : HTMLDivElement = document.createElement("div") as HTMLDivElement

    val content = mutableListOf<AdaptiveFragment>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(1)).apply { create() }
    }

    override fun genPatchInternal(): Boolean = true

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("before-addActual")

        check(fragment is AdaptiveBrowserFragment)

        content += fragment

        if (trace) trace("after-addActual")
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        check(fragment is AdaptiveBrowserFragment)

        content -= fragment

        if (trace) trace("after-removeActual")
    }

    override fun beforeMount() {
        with(receiver.style) {
            position = "relative"
            width = "400.px"
            height = "400.px"
        }
        parent?.addActual(this, null)
    }

    override fun afterUnmount() {
        parent?.removeActual(this)
    }

    fun layout() {
        val colTemp = checkNotNull(instructions.firstOrNullIfInstance<ColumnTemplate>()) { "missing column template in $this" }
        val rowTemp = checkNotNull(instructions.firstOrNullIfInstance<RowTemplate>()) { "missing row template in $this" }

        val availableWidth = 400f
        val availableHeight = 400f

        val columnSizes = distribute(availableWidth, expand(colTemp.tracks))
        val rowSizes = distribute(availableHeight, expand(rowTemp.tracks))


    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGrid"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGrid(parent.adapter, parent, index)

    }

}