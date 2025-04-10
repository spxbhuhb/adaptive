/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveFragmentFactory

open class FoundationFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("foundation:measurefragmenttime") { p, i, s -> FoundationMeasureFragmentTime(p.adapter, p, i) }
        add("foundation:localcontext") { p, i, s -> FoundationLocalContext(p.adapter, p, i) }
        add("foundation:actualize") { p, i, s -> FoundationActualize(p.adapter, p, i) }
    }
}