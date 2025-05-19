/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.builtin

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@AdaptiveExpect(backend)
fun worker(vararg instructions: AdaptiveInstruction, impl: () -> WorkerImpl<*>): AdaptiveFragment {
    manualImplementation(instructions, impl)
}

@AdaptiveActual
class BackendWorker(
    adapter: BackendAdapter,
    parent: AdaptiveFragment,
    index: Int
) : BackendFragment(adapter, parent, index) {

    val workerImpl : WorkerImpl<*>?
        get() = impl as WorkerImpl<*>?

    val scope = CoroutineScope(SupervisorJob(adapter.scope.coroutineContext[Job]) + adapter.dispatcher)

    override fun mount() {
        if (trace) trace("before-Mount")

        checkNotNull(workerImpl) { "inconsistent backend state: innerMount with a null implementation" }
            .let {
                it.mount()
                scope.launch {
                    try {
                        it.run()
                    } catch (_: CancellationException) {
                        // this is OK (I think), I do cancel on unmount
                    } catch (ex: Exception) {
                        it.logger.error(ex)
                    }
                }
            }

        if (trace) trace("after-Mount")
    }

    override fun unmount() {
        if (trace) trace("before-Unmount")

        checkNotNull(impl) { "inconsistent backend state innerUnmount with a null implementation" }
            .also { it.unmount() }

        if (scope.isActive) {
            scope.cancel("unmount") // TODO check Job docs about waiting
        }

        impl = null

        if (trace) trace("after-Unmount")
    }

}