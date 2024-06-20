/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.canvas

import hu.simplexion.adaptive.foundation.fragment.FoundationFragmentFactory
import hu.simplexion.adaptive.grapics.canvas.fragment.CanvasCircle
import hu.simplexion.adaptive.grapics.canvas.fragment.CanvasSvg

object CanvasFragmentFactory : FoundationFragmentFactory() {
    init {
        add("canvas:circle") { p,i -> CanvasCircle(p.adapter as CanvasAdapter, p, i) }
        add("canvas:svg") { p,i -> CanvasSvg(p.adapter as CanvasAdapter, p, i) }
    }
}