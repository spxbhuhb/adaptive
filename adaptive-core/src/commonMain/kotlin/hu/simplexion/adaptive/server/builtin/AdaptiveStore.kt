/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.builtin

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerFragment

@Adaptive
fun store(impl : () -> StoreImpl<*>) {
    manualImplementation(impl)
}

class AdaptiveStore<BT>(
    adapter: AdaptiveServerAdapter<BT>,
    parent: AdaptiveFragment<BT>,
    index: Int
) : AdaptiveServerFragment<BT>(adapter, parent, index) {

    override fun innerMount(bridge: AdaptiveBridge<BT>) {
        impl?.let {
            it.mount()
            serverAdapter.storeCache[it.classFqName] = this
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun innerUnmount(bridge: AdaptiveBridge<BT>) {
        impl?.let {
            serverAdapter.storeCache.remove(it.classFqName)
            if (it is AutoCloseable) it.close()
        }
        impl = null
    }

}