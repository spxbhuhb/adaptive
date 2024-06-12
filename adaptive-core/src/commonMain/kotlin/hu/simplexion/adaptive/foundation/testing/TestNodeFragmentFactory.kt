/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory

object TestNodeFragmentFactory : AdaptiveFragmentFactory() {
    init {
        set("test:T0") { p,i -> AdaptiveT0(p.adapter as AdaptiveTestAdapter, p, i) }
        set("test:T1") { p,i -> AdaptiveT1(p.adapter as AdaptiveTestAdapter, p, i) }
        set("test:S1") { p,i -> AdaptiveS1(p.adapter as AdaptiveTestAdapter, p, i) }
        set("test:S1R") { p,i -> AdaptiveS1R(p.adapter as AdaptiveTestAdapter, p, i) }
        set("test:SuspendS1") { p,i -> AdaptiveSuspendS1(p.adapter as AdaptiveTestAdapter, p, i) }
    }
}