/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.testing

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object TestNodeFragmentFactory : FoundationFragmentFactory() {
    init {
        add("test:t0") { p, i, s -> AdaptiveT0(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:t1") { p, i, s -> AdaptiveT1(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:s1") { p, i, s -> AdaptiveS1(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:s1r") { p, i, s -> AdaptiveS1R(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:suspends1") { p, i, s -> AdaptiveSuspendS1(p.adapter as AdaptiveTestAdapter, p, i) }
    }
}