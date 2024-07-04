/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.builtin

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerFragment
import hu.simplexion.adaptive.server.server

@AdaptiveExpect(server)
fun store(vararg instructions: AdaptiveInstruction, impl: () -> StoreImpl<*>): AdaptiveFragment {
    manualImplementation(instructions, impl)
}

@AdaptiveActual
class ServerStore(
    adapter: AdaptiveServerAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index) {

    override fun mount() {
        if (trace) trace("before-Mount")

        impl?.mount()

        if (trace) trace("after-Mount")
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun unmount() {
        if (trace) trace("before-Unmount")

        impl?.let {
            if (it is AutoCloseable) it.close()
        }
        impl = null

        if (trace) trace("after-Unmount")
    }

}