/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.ui.common.fragment.CommonImage
import `fun`.adaptive.ui.common.fragment.CommonText
import `fun`.adaptive.ui.common.fragment.layout.*
import `fun`.adaptive.ui.common.fragment.structural.CommonSlot

object CommonFragmentFactory : FoundationFragmentFactory() {
    init {
        add("common:box") { p, i -> CommonBox(p.adapter as CommonAdapter, p, i) }
        add("common:column") { p, i -> CommonColumn(p.adapter as CommonAdapter, p, i) }
        add("common:flowbox") { p, i -> CommonFlowBox(p.adapter as CommonAdapter, p, i) }
        add("common:grid") { p, i -> CommonGrid(p.adapter as CommonAdapter, p, i) }
        add("common:image") { p, i -> CommonImage(p.adapter as CommonAdapter, p, i) }
        add("common:row") { p, i -> CommonRow(p.adapter as CommonAdapter, p, i) }
        add("common:slot") { p, i -> CommonSlot(p.adapter as CommonAdapter, p, i) }
        add("common:text") { p, i -> CommonText(p.adapter as CommonAdapter, p, i) }
    }
}