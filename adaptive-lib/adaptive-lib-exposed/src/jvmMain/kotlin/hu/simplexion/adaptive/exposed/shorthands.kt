/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment
import hu.simplexion.adaptive.server.builtin.worker

@Adaptive
fun inMemoryH2(): AdaptiveFragment {
    worker { InMemoryDatabase() }
    return fragment()
}