/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg

import hu.simplexion.adaptive.foundation.fragment.FoundationFragmentFactory
import hu.simplexion.adaptive.grapics.svg.fragment.SvgGroup
import hu.simplexion.adaptive.grapics.svg.fragment.SvgPath
import hu.simplexion.adaptive.grapics.svg.fragment.SvgRoot

object SvgFragmentFactory : FoundationFragmentFactory() {
    init {
        add("svg:root") { p, i -> SvgRoot(p.adapter as SvgAdapter, p, i) }
        add("svg:group") { p, i -> SvgGroup(p.adapter as SvgAdapter, p, i) }
        add("svg:path") { p, i -> SvgPath(p.adapter as SvgAdapter, p, i) }
    }
}