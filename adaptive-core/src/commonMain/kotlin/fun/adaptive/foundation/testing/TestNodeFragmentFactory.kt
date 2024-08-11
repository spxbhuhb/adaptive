/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.testing

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object TestNodeFragmentFactory : FoundationFragmentFactory() {
    init {
        add("test:t0") { p,i -> AdaptiveT0(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:t1") { p,i -> AdaptiveT1(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:s1") { p,i -> AdaptiveS1(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:s1r") { p,i -> AdaptiveS1R(p.adapter as AdaptiveTestAdapter, p, i) }
        add("test:suspends1") { p,i -> AdaptiveSuspendS1(p.adapter as AdaptiveTestAdapter, p, i) }
    }
}