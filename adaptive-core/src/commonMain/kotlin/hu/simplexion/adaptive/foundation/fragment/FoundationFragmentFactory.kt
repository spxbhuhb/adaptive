/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory

open class FoundationFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("foundation:slot") { p, i -> FoundationSlot(p.adapter, p, i) }
        add("foundation:measureFragmentTime") { p, i -> FoundationMeasureFragmentTime(p.adapter, p, i) }
    }
}