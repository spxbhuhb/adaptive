package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.utility.waitForReal
import kotlin.time.Duration.Companion.seconds

class TestSetup<OFE : FrontendBase, CFE : FrontendBase>(
    originAdapter : BackendAdapter,
    connectingAdapter : BackendAdapter,
) {

    val originWorker = originAdapter.firstImpl<AutoWorker>()
    val originBackend = originWorker.backends.values.first()

    val connectingWorker = connectingAdapter.firstImpl<AutoWorker>()
    val connectingBackend = connectingWorker.backends.values.first()

    @Suppress("UNCHECKED_CAST")
    val originFrontend : OFE = originBackend.frontend as OFE

    @Suppress("UNCHECKED_CAST")
    val connectingFrontend : CFE = originBackend.frontend as CFE

    suspend fun waitForSync() {
        waitForReal(2.seconds) {
           originWorker.peerTime(originBackend.context.handle).timestamp != connectingWorker.peerTime(connectingBackend.context.handle).timestamp
        }
    }

}