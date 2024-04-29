/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerBridgeReceiver
import hu.simplexion.adaptive.server.AdaptiveServerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val workerScope = CoroutineScope(Dispatchers.Default)

fun Adaptive.worker(impl : () -> WorkerImpl<*>) {
    manualImplementation(impl)
}

class AdaptiveWorker(
    adapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver>,
    parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index, 1) {

    private val implFun : AdaptiveSupportFunction
        get() = state[0] as AdaptiveSupportFunction

    private val scope = CoroutineScope(adapter.dispatcher)

    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        scope.launch { (implFun.invoke() as WorkerImpl<*>).run(scope) }
    }

    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        scope.cancel()
    }

}