package hu.simplexion.z2.server.adaptive.components

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.server.adaptive.AdaptiveServerBridgeReceiver
import hu.simplexion.z2.server.adaptive.AdaptiveServerFragment
import hu.simplexion.z2.server.worker.WorkerImpl
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

    private val impl : WorkerImpl<*>
        get() = state[0] as WorkerImpl<*>

    private val scope = CoroutineScope(adapter.dispatcher)

    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        scope.launch { impl.run(this ) }
    }

    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {
        scope.cancel()
    }

}