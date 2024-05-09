/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.base.registry

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment

class AdaptiveBindingImplRegistryEntry<BT>(
    val name: String,
    val build: (adapter: AdaptiveAdapter<BT>, parent: AdaptiveFragment<BT>, index: Int) -> AdaptiveFragment<BT>
)