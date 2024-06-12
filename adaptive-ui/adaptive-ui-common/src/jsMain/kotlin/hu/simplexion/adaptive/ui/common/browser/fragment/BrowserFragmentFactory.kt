/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter

object BrowserFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("common:box") { p,i -> AdaptiveBox(p.adapter as AdaptiveBrowserAdapter, p, i) }
        add("common:canvas") { p,i -> AdaptiveCanvas(p.adapter as AdaptiveBrowserAdapter, p, i) }
        add("common:column") { p,i -> AdaptiveColumn(p.adapter as AdaptiveBrowserAdapter, p, i) }
        add("common:grid") { p,i -> AdaptiveGrid(p.adapter as AdaptiveBrowserAdapter, p, i) }
        add("common:image") { p,i -> AdaptiveImage(p.adapter as AdaptiveBrowserAdapter, p, i) }
        add("common:input") { p,i -> AdaptiveInput(p.adapter as AdaptiveBrowserAdapter, p, i) }
        add("common:row") { p,i -> AdaptiveRow(p.adapter as AdaptiveBrowserAdapter, p, i) }
        add("common:text") { p,i -> AdaptiveText(p.adapter as AdaptiveBrowserAdapter, p, i) }
    }
}