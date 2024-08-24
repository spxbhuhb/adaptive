/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.testing

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.fragment.structural.AuiSlot
import `fun`.adaptive.ui.testing.fragment.*

object AuiFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("aui:box") { p, i -> AdaptiveBox(p.adapter as AuiTestAdapter, p, i) }
        add("aui:column") { p, i -> AdaptiveColumn(p.adapter as AuiTestAdapter, p, i) }
        add("aui:grid") { p, i -> AdaptiveGrid(p.adapter as AuiTestAdapter, p, i) }
        add("aui:image") { p, i -> AdaptiveImage(p.adapter as AuiTestAdapter, p, i) }
        add("aui:row") { p, i -> AdaptiveRow(p.adapter as AuiTestAdapter, p, i) }
        add("aui:space") { p, i -> AuiSpace(p.adapter as AuiTestAdapter, p, i) }
        add("aui:slot") { p, i -> AuiSlot(p.adapter as AuiTestAdapter, p, i) }
        add("aui:text") { p, i -> AdaptiveText(p.adapter as AuiTestAdapter, p, i) }
    }
}