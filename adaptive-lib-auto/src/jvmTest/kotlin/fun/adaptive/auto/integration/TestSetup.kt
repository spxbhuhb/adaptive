package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.wireformat.json.JsonWireFormatProvider
import kotlinx.io.files.Path
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
    val originFrontend : OFE = originBackend.frontEnd as OFE

    @Suppress("UNCHECKED_CAST")
    val connectingFrontend : CFE = originBackend.frontEnd as CFE

    suspend fun waitForSync() {
        waitForReal(2.seconds) {
           originWorker.peerTime(originBackend.context.handle).timestamp != connectingWorker.peerTime(connectingBackend.context.handle).timestamp
        }
    }

    fun read(path : Path) =
        FileFrontend.read(path, JsonWireFormatProvider()).second as TestData

}