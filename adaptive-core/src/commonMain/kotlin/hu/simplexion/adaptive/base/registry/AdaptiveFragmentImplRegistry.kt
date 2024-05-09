/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.base.registry

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment

class AdaptiveFragmentImplRegistry<BT> {
    val entries = mutableMapOf<String, AdaptiveFragmentImplRegistryEntry<BT>>()

    fun build(
        name : String,
        adapter: AdaptiveAdapter<BT>,
        parent : AdaptiveFragment<BT>,
        index : Int
    ): AdaptiveFragment<BT> =
        checkNotNull(entries[name]) { "No implementation found for $name" }
            .build(adapter, parent, index)

    operator fun plusAssign(entry: AdaptiveFragmentImplRegistryEntry<BT>) {
        entries[entry.name] = entry
    }
}

