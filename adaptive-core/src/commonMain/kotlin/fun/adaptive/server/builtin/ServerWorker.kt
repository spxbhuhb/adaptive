/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.server.builtin

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.server.AdaptiveServerAdapter
import `fun`.adaptive.server.AdaptiveServerFragment
import `fun`.adaptive.server.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AdaptiveExpect(server)
fun worker(vararg instructions: AdaptiveInstruction, impl: () -> WorkerImpl<*>): AdaptiveFragment {
    manualImplementation(instructions, impl)
}

@AdaptiveActual
class ServerWorker(
    adapter: AdaptiveServerAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index) {

    val workerImpl : WorkerImpl<*>?
        get() = impl as WorkerImpl<*>?

    val scope = CoroutineScope(adapter.dispatcher)

    override fun mount() {
        if (trace) trace("before-Mount")

        checkNotNull(workerImpl) { "inconsistent server state: innerMount with a null implementation" }
            .let {
                it.mount()
                scope.launch {
                    try {
                        it.run()
                    } catch (ex: Exception) {
                        it.logger.error(ex)
                    }
                }
            }

        if (trace) trace("after-Mount")
    }

    override fun unmount() {
        if (trace) trace("before-Unmount")

        checkNotNull(impl) { "inconsistent server state innerUnmount with a null implementation" }
            .also { it.unmount() }

        scope.cancel() // TODO check Job docs about waiting
        impl = null

        if (trace) trace("after-Unmount")
    }

}