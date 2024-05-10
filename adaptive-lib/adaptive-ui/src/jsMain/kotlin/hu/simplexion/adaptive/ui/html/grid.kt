/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.html

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.ui.css.AdaptiveCssStyle
import hu.simplexion.adaptive.ui.css.display_grid
import hu.simplexion.adaptive.ui.dom.AdaptiveDOMNodeFragment
import hu.simplexion.adaptive.base.manualImplementation
import hu.simplexion.adaptive.base.structural.AdaptiveAnonymous
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Node

fun Adaptive.grid(templateColumns: String, templateRows: String, vararg styles: AdaptiveCssStyle, builder: Adaptive.() -> Unit) {
    manualImplementation(AdaptiveGrid::class, templateColumns, templateColumns, templateRows, styles, builder)
}

class AdaptiveGrid(
    adapter: AdaptiveAdapter<Node>,
    parent: AdaptiveFragment<Node>,
    index: Int
) : AdaptiveDOMNodeFragment(adapter, parent, index, 4, false) {

    override val receiver = document.createElement("div") as HTMLDivElement

    private val templateColumns get() = state[0] as String
    private val templateRows get() = state[1] as String
    private val styles get() = getStyles(2)
    private val builder get() = getFragmentFactory(3)

    override fun genBuild(parent: AdaptiveFragment<Node>, declarationIndex: Int): AdaptiveFragment<Node> =
        AdaptiveAnonymous(adapter, parent, declarationIndex, 0, builder).also { it.create() }

    override fun genPatchInternal() {
        if (firstTimeInit) {
            receiver.style.setProperty("grid-template-columns", templateColumns)
            receiver.style.setProperty("grid-template-rows", templateRows)
            addClass(styles + display_grid)
        }
    }

}