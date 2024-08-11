/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.server

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.server.builtin.ServerService
import `fun`.adaptive.server.builtin.ServerStore
import `fun`.adaptive.server.builtin.ServerWorker

object ServerFragmentFactory : FoundationFragmentFactory() {
    init {
        add("server:service") { p, i -> ServerService(p.adapter as AdaptiveServerAdapter, p, i) }
        add("server:store") { p, i -> ServerStore(p.adapter as AdaptiveServerAdapter, p, i) }
        add("server:worker") { p, i -> ServerWorker(p.adapter as AdaptiveServerAdapter, p, i) }
    }
}