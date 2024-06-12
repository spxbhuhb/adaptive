/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.ui.common.uikit.AdaptiveIosAdapter

object UiKitFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("common:box") { p, i -> AdaptiveBox(p.adapter as AdaptiveIosAdapter, p, i) }
        add("common:column") { p, i -> AdaptiveColumn(p.adapter as AdaptiveIosAdapter, p, i) }
        add("common:grid") { p, i -> AdaptiveGrid(p.adapter as AdaptiveIosAdapter, p, i) }
        add("common:image") { p, i -> AdaptiveImage(p.adapter as AdaptiveIosAdapter, p, i) }
        add("common:row") { p, i -> AdaptiveRow(p.adapter as AdaptiveIosAdapter, p, i) }
        add("common:text") { p, i -> AdaptiveText(p.adapter as AdaptiveIosAdapter, p, i) }
    }
}