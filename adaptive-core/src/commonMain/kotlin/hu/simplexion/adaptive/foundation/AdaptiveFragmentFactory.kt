/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.internal.NamedFragmentFactory
import hu.simplexion.adaptive.registry.Registry

open class AdaptiveFragmentFactory : Registry<NamedFragmentFactory>() {

    fun set(key : String, buildFun : (parent : AdaptiveFragment, index : Int) -> AdaptiveFragment) {
        set(key, NamedFragmentFactory(key, buildFun))
    }

    fun newInstance(key: String, parent: AdaptiveFragment, index: Int): AdaptiveFragment {
        return checkNotNull(get(key)) { "Unknown fragment type: $key" }.build(parent, index)
    }

}