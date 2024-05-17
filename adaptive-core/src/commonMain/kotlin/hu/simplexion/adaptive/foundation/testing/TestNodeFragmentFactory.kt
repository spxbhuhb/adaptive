/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory

object TestNodeFragmentFactory : AdaptiveFragmentFactory() {
    init {
        addAll(AdaptiveT0, AdaptiveT1, AdaptiveS1, AdaptiveS1R, AdaptiveSuspendS1)
    }
}