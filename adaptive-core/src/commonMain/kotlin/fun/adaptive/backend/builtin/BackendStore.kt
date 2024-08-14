/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.builtin

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.backend.backend

@AdaptiveExpect(backend)
fun store(vararg instructions: AdaptiveInstruction, impl: () -> StoreImpl<*>): AdaptiveFragment {
    manualImplementation(instructions, impl)
}

@AdaptiveActual
class BackendStore(
    adapter: BackendAdapter,
    parent: AdaptiveFragment,
    index: Int
) : BackendFragment(adapter, parent, index) {

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