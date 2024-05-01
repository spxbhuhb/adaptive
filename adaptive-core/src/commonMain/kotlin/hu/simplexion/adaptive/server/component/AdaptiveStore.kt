/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.component

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerFragment

fun Adaptive.store(impl : () -> StoreImpl<*,*>) {
    manualImplementation(impl)
}

class AdaptiveStore<BT>(
    adapter: AdaptiveServerAdapter<BT>,
    parent: AdaptiveFragment<BT>,
    index: Int
) : AdaptiveServerFragment<BT, StoreImpl<*,BT>>(adapter, parent, index) {

    override fun innerMount(bridge: AdaptiveBridge<BT>) {
        impl?.mount()
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun innerUnmount(bridge: AdaptiveBridge<BT>) {
        impl?.let {
            if (it is AutoCloseable) it.close()
        }
        impl = null
    }

}