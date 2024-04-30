/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerBridgeReceiver
import hu.simplexion.adaptive.server.AdaptiveServerFragment

fun Adaptive.store(impl : () -> StoreImpl<*>) {
    manualImplementation(impl)
}

class AdaptiveStore(
    adapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver>,
    parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index) {

    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        (implFun.invoke() as ServerFragmentImpl).also {
            impl = it
            it.serverAdapter = adapter as AdaptiveServerAdapter
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        impl?.let {
            if (it is AutoCloseable) it.close()
        }
        impl = null
    }

}