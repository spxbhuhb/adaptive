/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(aui)
open class AuiRootBox(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractBox<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex) {

    override val isRootActual: Boolean
        get() = true

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        super.computeLayout(proposedWidth, proposedHeight)
        println("$proposedWidth $proposedHeight ${renderData.finalWidth} ${renderData.finalHeight} ")
    }
}