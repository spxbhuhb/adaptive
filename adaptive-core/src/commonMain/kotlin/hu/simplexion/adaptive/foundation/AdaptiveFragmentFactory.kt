/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.registry.Registry

open class AdaptiveFragmentFactory<BT> : Registry<AdaptiveFragmentCompanion<BT>>() {

    fun addAll(vararg companions : AdaptiveFragmentCompanion<BT>) {
        for (companion in companions) {
            set(companion.name, companion)
        }
    }

    fun newInstance(name: String, parent: AdaptiveFragment<BT>, index: Int): AdaptiveFragment<BT> {
        return checkNotNull(get(name)) { "Unknown fragment type: $name" }.newInstance(parent, index)
    }

}