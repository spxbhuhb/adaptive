/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.exposed

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.backend.builtin.worker

@Adaptive
fun hikari(): AdaptiveFragment {
    worker { HikariWorker() }
    return fragment()
}

@Adaptive
fun inMemoryH2(name: String = "db"): AdaptiveFragment {
    worker { InMemoryDatabase(name) }
    return fragment()
}