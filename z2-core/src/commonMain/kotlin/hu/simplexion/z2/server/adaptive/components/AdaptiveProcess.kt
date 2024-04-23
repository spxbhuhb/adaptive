package hu.simplexion.z2.server.adaptive.components

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.server.adaptive.AdaptiveServerBridgeReceiver
import hu.simplexion.z2.server.adaptive.AdaptiveServerFragment
import hu.simplexion.z2.server.worker.WorkerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

fun Adaptive.process(impl : ProcessBuilder.() -> Unit) {
    manualImplementation(impl)
}

class AdaptiveProcess(
    adapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver>,
    parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index, 1) {

    val command : String
        get() = state[0] as String


    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {

    }

    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {

    }

}

class ProcessBuilder()