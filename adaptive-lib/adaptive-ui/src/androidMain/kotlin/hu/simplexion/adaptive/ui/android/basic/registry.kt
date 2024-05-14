/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.android.basic

import android.view.View
import hu.simplexion.adaptive.base.registry.AdaptiveFragmentImplRegistry
import hu.simplexion.adaptive.base.registry.AdaptiveFragmentImplRegistryEntry
import org.w3c.dom.Node

object BasicRegistry : AdaptiveFragmentImplRegistry<View>() {
    init {
        this += AdaptiveFragmentImplRegistryEntry("hu.simplexion.adaptive.ui.basic.AdaptiveText") { a, p, i -> AdaptiveText(a, p, i) }
    }
}