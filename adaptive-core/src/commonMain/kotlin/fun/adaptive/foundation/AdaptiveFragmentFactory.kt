/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation

import `fun`.adaptive.foundation.internal.NamedFragmentFactory
import `fun`.adaptive.registry.Registry
import `fun`.adaptive.utility.PluginReference

/**
 * Fragment factories create new instances of fragment based on a key. This supports the expect / actual pattern,
 * where the declaration of a fragment is separated from the implementation.
 *
 * `stateSize` is typically used for fragment hydration where actual size of the state depends on the design
 * being hydrated.
 */
open class AdaptiveFragmentFactory : Registry<NamedFragmentFactory>() {

    fun add(key: String, buildFun: (parent: AdaptiveFragment, declarationIndex: Int, stateSize : Int) -> AdaptiveFragment) {
        set(key, NamedFragmentFactory(key, buildFun))
    }

    @PluginReference("addNonTransformed")
    fun add(key : String, @Adaptive f : () -> AdaptiveFragment) {
        set(key, NamedFragmentFactory(key) { _, _, _ -> f() })
    }

    @PluginReference("addTransformed")
    fun add(key : String, f : (AdaptiveFragment, Int) -> AdaptiveFragment) {
        set(key, NamedFragmentFactory(key) { p, i, _ -> f(p, i) })
    }

    fun newInstance(key: String, parent: AdaptiveFragment, declarationIndex: Int, stateSize : Int): AdaptiveFragment {
        return checkNotNull(get(key)) { "Unknown fragment type: $key, known fragment types: ${entries.keys}" }
            .build(parent, declarationIndex, stateSize)
    }

}