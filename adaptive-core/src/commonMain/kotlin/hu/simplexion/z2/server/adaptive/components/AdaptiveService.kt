package hu.simplexion.z2.server.adaptive.components

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.server.adaptive.AdaptiveServerBridgeReceiver
import hu.simplexion.z2.server.adaptive.AdaptiveServerFragment
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.defaultServiceImplFactory

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