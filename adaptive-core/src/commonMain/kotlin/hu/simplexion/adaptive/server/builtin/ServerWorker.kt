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
        scope.cancel() // TODO check Job docs about waiting
        impl = null

        if (trace) trace("after-Unmount")
    }

}