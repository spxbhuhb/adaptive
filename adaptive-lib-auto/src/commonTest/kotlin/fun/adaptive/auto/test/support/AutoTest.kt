package `fun`.adaptive.auto.test.support

import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AutoTest {

    val clientTransport = DirectServiceTransport(name = "client") // .also { it.trace = true; it.transportLog.enableFine() }
    val serverTransport = DirectServiceTransport(name = "server") // .also { it.trace = true; it.transportLog.enableFine() }

    init {
        clientTransport.peerTransport = serverTransport
        serverTransport.peerTransport = clientTransport
    }

    lateinit var serverBackend: BackendAdapter

    lateinit var clientBackend: BackendAdapter

    val serverWorker
        get() = serverBackend.firstImpl<AutoWorker>()

    val clientWorker
        get() = clientBackend.firstImpl<AutoWorker>()

    companion object {

        @OptIn(ExperimentalCoroutinesApi::class)
        fun autoTest(timeout: Duration = 10.seconds, testFun: suspend AutoTest.() -> Unit) =

            runTest(timeout = timeout) {
                with(AutoTest()) {

                    // Switch to a coroutine context that is NOT a test context. The test context
                    // skips delays which wreaks havoc with service call timeouts that depend on
                    // delays actually working.

                    val serverDispatcher = Dispatchers.Default
                    val serverScope = CoroutineScope(serverDispatcher)

                    val clientDispatcher = Dispatchers.Default.limitedParallelism(1)
                    val clientScope = CoroutineScope(clientDispatcher)

                    serverBackend = backend(serverTransport, dispatcher = serverDispatcher, scope = serverScope) {
                        auto()
                    }

                    clientBackend = backend(clientTransport, dispatcher = clientDispatcher, scope = clientScope) {
                        auto()
                    }

                    withContext(clientDispatcher) {
                        testFun()
                    }

                    clientBackend.stop()
                    serverBackend.stop()
                }
            }

        suspend fun sync(i1: AutoGeneric, i2: AutoGeneric, duration: Duration = 1.seconds) {
            waitForReal(duration) { i1.time.timestamp == i2.time.timestamp }
        }
    }
}