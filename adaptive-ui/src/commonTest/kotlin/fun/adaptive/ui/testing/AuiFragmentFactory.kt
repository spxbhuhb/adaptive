/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.testing

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.testing.fragment.*

object AuiFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("aui:box") { p, i, s -> AdaptiveBox(p.adapter as AuiTestAdapter, p, i) }
        add("aui:column") { p, i, s -> AdaptiveColumn(p.adapter as AuiTestAdapter, p, i) }
        add("aui:grid") { p, i, s -> AdaptiveGrid(p.adapter as AuiTestAdapter, p, i) }
        add("aui:image") { p, i, s -> AdaptiveImage(p.adapter as AuiTestAdapter, p, i) }
        add("aui:row") { p, i, s -> AdaptiveRow(p.adapter as AuiTestAdapter, p, i) }
        add("aui:space") { p, i, s -> AuiSpace(p.adapter as AuiTestAdapter, p, i) }
        add("aui:text") { p, i, s -> AdaptiveText(p.adapter as AuiTestAdapter, p, i) }
    }
}