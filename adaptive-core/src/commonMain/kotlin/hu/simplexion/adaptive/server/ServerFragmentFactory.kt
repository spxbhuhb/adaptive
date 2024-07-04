/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.foundation.fragment.FoundationFragmentFactory
import hu.simplexion.adaptive.server.builtin.ServerService
import hu.simplexion.adaptive.server.builtin.ServerStore
import hu.simplexion.adaptive.server.builtin.ServerWorker

object ServerFragmentFactory : FoundationFragmentFactory() {
    init {
        add("server:service") { p, i -> ServerService(p.adapter as AdaptiveServerAdapter, p, i) }
        add("server:store") { p, i -> ServerStore(p.adapter as AdaptiveServerAdapter, p, i) }
        add("server:worker") { p, i -> ServerWorker(p.adapter as AdaptiveServerAdapter, p, i) }
    }
}