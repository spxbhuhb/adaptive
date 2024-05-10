/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.html

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.css.AdaptiveCssStyle
import hu.simplexion.adaptive.dom.AdaptiveDOMNodeFragment
import hu.simplexion.adaptive.base.manualImplementation
import hu.simplexion.adaptive.base.structural.AdaptiveAnonymous
import kotlinx.browser.document
import org.w3c.dom.Node

@Adaptive
fun div(vararg styles : AdaptiveCssStyle, @Adaptive builder : () -> Unit) {
    manualImplementation(AdaptiveDiv::class, styles, builder)
}

class AdaptiveDiv(
    adapter: AdaptiveAdapter<Node>,
    parent : AdaptiveFragment<Node>,
    index : Int
) : AdaptiveDOMNodeFragment(adapter, parent, index, 2, false) {

    override val receiver = document.createElement("div")

    private val styles get() = getStyles(0)
    private val builder get() = getFragmentFactory(1)

    override fun genBuild(parent: AdaptiveFragment<Node>, declarationIndex: Int): AdaptiveFragment<Node> =
        AdaptiveAnonymous(adapter, parent, declarationIndex, 0, builder).also { it.create() }

    override fun genPatchInternal() {
        if (firstTimeInit) {
            addClass(styles)
        }
    }

}