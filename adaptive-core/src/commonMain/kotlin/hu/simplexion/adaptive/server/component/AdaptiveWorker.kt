/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.component

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerBridgeReceiver
import hu.simplexion.adaptive.server.AdaptiveServerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val workerScope = CoroutineScope(Dispatchers.Default)

fun Adaptive.worker(impl: () -> WorkerImpl<*>) {
    manualImplementation(impl)
}

class AdaptiveWorker<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>,
    index: Int
) : AdaptiveServerFragment<BT,WorkerImpl<*>>(adapter, parent, index) {

    private val scope = CoroutineScope(adapter.dispatcher)

    override fun innerMount(bridge: AdaptiveBridge<BT>) {
        checkNotNull(impl) { "inconsistent server state: innerMount with a null implementation" }
            .let {
                it.mount()
                scope.launch { it.run(scope) }
            }
    }

    override fun innerUnmount(bridge: AdaptiveBridge<BT>) {
        check(impl != null) { "inconsistent server state innerUnmount with a null implementation" }
        scope.cancel() // TODO check Job docs about waiting
        impl = null
    }

}