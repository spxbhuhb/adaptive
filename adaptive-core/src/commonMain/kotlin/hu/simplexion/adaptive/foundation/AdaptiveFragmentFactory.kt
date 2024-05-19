/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.registry.Registry

open class AdaptiveFragmentFactory : Registry<AdaptiveFragmentCompanion>() {

    fun addAll(vararg companions : AdaptiveFragmentCompanion) {
        for (companion in companions) {
            set(companion.fragmentType, companion)
        }
    }

    fun newInstance(key: String, parent: AdaptiveFragment, index: Int): AdaptiveFragment {
        return checkNotNull(get(key)) { "Unknown fragment type: $key" }.newInstance(parent, index)
    }

}