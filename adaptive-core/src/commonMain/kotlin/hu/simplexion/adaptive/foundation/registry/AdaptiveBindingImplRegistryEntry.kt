/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.registry

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment

class AdaptiveBindingImplRegistryEntry<BT>(
    val name: String,
    val build: (adapter: AdaptiveAdapter<BT>, parent: AdaptiveFragment<BT>, index: Int) -> hu.simplexion.adaptive.foundation.AdaptiveFragment<BT>
)