/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.svg

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.ui.svg.fragments.Circle

object SvgFragmentFactory : AdaptiveFragmentFactory() {
    init {
        set("svg:Circle") { p,i -> Circle(p.adapter as SvgAdapter, p, i) }
    }
}