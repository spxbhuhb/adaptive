/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.internal.NamedFragmentFactory
import hu.simplexion.adaptive.registry.Registry

open class AdaptiveFragmentFactory : Registry<NamedFragmentFactory>() {

    fun add(key: String, buildFun: (parent: AdaptiveFragment, declarationIndex: Int) -> AdaptiveFragment) {
        set(key, NamedFragmentFactory(key, buildFun))
    }

    fun newInstance(key: String, parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return checkNotNull(get(key)) { "Unknown fragment type: $key, known fragment types: ${entries.keys}" }
            .build(parent, declarationIndex)
    }

}