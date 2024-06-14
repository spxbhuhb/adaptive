/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.canvas

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.grapics.canvas.fragment.CanvasCircle

object CanvasFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("canvas:circle") { p,i -> CanvasCircle(p.adapter as CanvasAdapter, p, i) }
    }
}