/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.base.*
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
) : AdaptiveServerFragment(adapter, parent, index, 1) {

    val impl : ServiceImpl<*>
        get() = state[0] as ServiceImpl<*>

    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        defaultServiceImplFactory += impl
    }

    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        defaultServiceImplFactory -= impl
    }

}