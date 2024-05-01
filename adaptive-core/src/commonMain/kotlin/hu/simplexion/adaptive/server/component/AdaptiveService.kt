/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.component

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerFragment
import hu.simplexion.adaptive.service.ServiceImpl
import hu.simplexion.adaptive.service.defaultServiceImplFactory

fun Adaptive.service(impl: () -> ServiceImpl<*,*>) {
    manualImplementation(impl)
}

class AdaptiveService<BT>(
    adapter: AdaptiveServerAdapter<BT>,
    parent: AdaptiveFragment<BT>,
    index: Int
) : AdaptiveServerFragment<BT,ServiceImpl<*,BT>>(adapter, parent, index) {

    override fun innerMount(bridge: AdaptiveBridge<BT>) {
        impl?.let {
            it.mount()
            defaultServiceImplFactory += it
        }
    }

    override fun innerUnmount(bridge: AdaptiveBridge<BT>) {
        defaultServiceImplFactory -= checkNotNull(impl) { "inconsistent server state innerUnmount with a null implementation" }
        impl = null
    }

}