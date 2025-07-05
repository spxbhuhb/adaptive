/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.graphics.svg.fragment.SvgGroup
import `fun`.adaptive.graphics.svg.fragment.SvgPath
import `fun`.adaptive.graphics.svg.fragment.SvgRoot
import `fun`.adaptive.graphics.svg.fragment.SvgSvg
import `fun`.adaptive.ui.AuiBrowserAdapter

object SvgFragmentFactory : FoundationFragmentFactory() {
    init {
        add("svg:root") { p, i, s -> SvgRoot(p.adapter as SvgAdapter, p, i) }
        add("svg:group") { p, i, s -> SvgGroup(p.adapter as SvgAdapter, p, i) }
        add("svg:path") { p, i, s -> SvgPath(p.adapter as SvgAdapter, p, i) }
        add("svg:svg") { p, i, s -> SvgSvg(p.adapter as AuiBrowserAdapter, p, i) }
    }
}