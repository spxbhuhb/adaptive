/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.svg.fragments

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.canvas.AdaptiveCanvasContext
import hu.simplexion.adaptive.ui.svg.SvgAdapter
import hu.simplexion.adaptive.ui.svg.SvgFragment
import hu.simplexion.adaptive.ui.svg.svg

@AdaptiveActual(svg)
open class Circle(
    adapter: SvgAdapter,
    parent: AdaptiveFragment,
    index: Int
) : SvgFragment(adapter, parent, index, -1, 0) {

    override fun draw(ctx : AdaptiveCanvasContext) {

    }

}