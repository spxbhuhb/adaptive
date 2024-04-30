/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

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

class AdaptiveWorker(
    adapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver>,
    parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index) {

    private val scope = CoroutineScope(adapter.dispatcher)

    override fun create() {
        super.create()
        check(impl == null) { "inconsistent server state: create with a non-null implementation" }
        (implFun.invoke() as WorkerImpl<*>).also {
            impl = it
            it.serverAdapter = adapter as AdaptiveServerAdapter
            it.create()
        }
    }

    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        checkNotNull(impl) { "inconsistent server state: innerMount with a null implementation" }
            .let {
                it.mount()
                scope.launch { (it as WorkerImpl<*>).run(scope) }
            }
    }

    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        check(impl != null) { "inconsistent server state innerUnmount with a null implementation" }
        scope.cancel() // TODO check Job docs about waiting
        impl = null
    }

}