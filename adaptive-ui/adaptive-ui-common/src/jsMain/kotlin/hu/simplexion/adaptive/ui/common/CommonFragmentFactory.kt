/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.fragment.FoundationFragmentFactory
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.fragment.structural.CommonSlot

object CommonFragmentFactory : FoundationFragmentFactory() {
    init {
        add("common:box") { p,i -> CommonBox(p.adapter as CommonAdapter, p, i) }
        add("common:canvas") { p,i -> CommonCanvas(p.adapter as CommonAdapter, p, i) }
        add("common:column") { p,i -> CommonColumn(p.adapter as CommonAdapter, p, i) }
        add("common:grid") { p,i -> CommonGrid(p.adapter as CommonAdapter, p, i) }
        add("common:image") { p,i -> CommonImage(p.adapter as CommonAdapter, p, i) }
        add("common:input") { p,i -> CommonInput(p.adapter as CommonAdapter, p, i) }
        add("common:row") { p,i -> CommonRow(p.adapter as CommonAdapter, p, i) }
        add("common:text") { p,i -> CommonText(p.adapter as CommonAdapter, p, i) }
        add("common:slot") { p, i -> CommonSlot(p.adapter as CommonAdapter, p, i) }
        add("common:svg") { p, i -> CommonSvg(p.adapter as CommonAdapter, p, i) }
    }
}