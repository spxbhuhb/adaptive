/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.basic

import hu.simplexion.adaptive.foundation.registry.AdaptiveFragmentImplRegistry
import hu.simplexion.adaptive.foundation.registry.AdaptiveFragmentImplRegistryEntry
import org.w3c.dom.Node

object BasicRegistry : AdaptiveFragmentImplRegistry<Node>() {
    init {
        this += AdaptiveFragmentImplRegistryEntry("hu.simplexion.adaptive.ui.basic.AdaptiveText") { a, p, i -> AdaptiveText(a, p, i) }
    }
}