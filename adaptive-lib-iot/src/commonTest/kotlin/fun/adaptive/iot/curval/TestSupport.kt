package `fun`.adaptive.iot.curval

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.service.testing.DirectServiceTransport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestSupport {

    val clientTransport = DirectServiceTransport(name = "client").also { it.trace = true; it.transportLog.enableFine() }
    val serverTransport = DirectServiceTransport(name = "server").also { it.trace = true; it.transportLog.enableFine() }

    init {
        clientTransport.peerTransport = serverTransport
        serverTransport.peerTransport = clientTransport
    }

    lateinit var serverBackend: BackendAdapter

    lateinit var clientBackend: BackendAdapter

    val serverWorker
        get() = serverBackend.firstImpl<CurValWorker>()

    val clientWorker
        get() = clientBackend.firstImpl<CurValWorker>()

    companion object {

        @OptIn(ExperimentalCoroutinesApi::class)
        fun curValTest(timeout: Duration = 10.seconds, testFun: suspend TestSupport.() -> Unit) =

            runTest(timeout = timeout) {
                with(TestSupport()) {

                    // Switch to a coroutine context that is NOT a test context. The test context
                    // skips delays which wreaks havoc with service call timeouts that depend on
                    // delays actually working.

                    val serverDispatcher = Dispatchers.Default
                    val serverScope = CoroutineScope(serverDispatcher)

                    val clientDispatcher = Dispatchers.Default.limitedParallelism(1)
                    val clientScope = CoroutineScope(clientDispatcher)

                    serverBackend = backend(serverTransport, dispatcher = serverDispatcher, scope = serverScope) {
                        worker { CurValWorker() }
                        service { CurValTestServerService() }
                    }

                    clientBackend = backend(clientTransport, dispatcher = clientDispatcher, scope = clientScope) {
                        worker { CurValWorker() }
                        service { CurValTestClientService() }
                    }

                    withContext(clientDispatcher) {
                        testFun()
                    }

                    clientBackend.stop()
                    serverBackend.stop()
                }
            }
    }
}