/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.registry

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment

open class AdaptiveFragmentImplRegistry<BT> {

    val entries = mutableMapOf<String, AdaptiveFragmentImplRegistryEntry<BT>>()

    fun actualize(
        name : String,
        parent : AdaptiveFragment<BT>,
        index : Int
    ): AdaptiveFragment<BT> =
        checkNotNull(entries[name]) { "No implementation found for $name" }
            .build(parent.adapter, parent, index)

    operator fun plusAssign(entry: AdaptiveFragmentImplRegistryEntry<BT>) {
        entries[entry.name] = entry
    }
}

