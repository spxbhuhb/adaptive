/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveFragmentFactory

open class FoundationFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("foundation:measurefragmenttime") { p, i -> FoundationMeasureFragmentTime(p.adapter, p, i) }
    }
}