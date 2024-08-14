/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.backend.builtin.BackendService
import `fun`.adaptive.backend.builtin.BackendStore
import `fun`.adaptive.backend.builtin.BackendWorker

object BackendFragmentFactory : FoundationFragmentFactory() {
    init {
        add("backend:service") { p, i -> BackendService(p.adapter as BackendAdapter, p, i) }
        add("backend:store") { p, i -> BackendStore(p.adapter as BackendAdapter, p, i) }
        add("backend:worker") { p, i -> BackendWorker(p.adapter as BackendAdapter, p, i) }
    }
}