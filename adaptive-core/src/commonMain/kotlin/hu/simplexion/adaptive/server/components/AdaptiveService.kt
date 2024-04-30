/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerBridgeReceiver
import hu.simplexion.adaptive.server.AdaptiveServerFragment
import hu.simplexion.adaptive.service.ServiceImpl
import hu.simplexion.adaptive.service.defaultServiceImplFactory

fun Adaptive.service(impl : () -> ServiceImpl<*>) {
    manualImplementation(impl)
}

class AdaptiveService(
    adapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver>,
    parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index) {

    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        check(impl == null) { "inconsistent server state innerMount with a non-null implementation" }
        (implFun.invoke() as ServiceImpl<*>).also {
            impl = it
            it.serverAdapter = adapter as AdaptiveServerAdapter
            defaultServiceImplFactory += it
        }
    }

    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        checkNotNull(impl) { "inconsistent server state innerUnmount with a null implementation" }.also {
            defaultServiceImplFactory -= it as ServiceImpl<*>
        }
        impl = null
    }

}