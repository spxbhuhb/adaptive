package `fun`.adaptive.auto.test.support

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.service.testing.DirectServiceTransport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

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

        fun autoTest(testFun: suspend AutoTest.() -> Unit) =

            runTest {
                with(AutoTest()) {

                    // Switch to a coroutine context that is NOT a test context. The test context
                    // skips delays which wreaks havoc with service call timeouts that depend on
                    // delays actually working.

                    withContext(Dispatchers.Default) {

                        serverBackend = backend(serverTransport) {
                            auto()
                        }

                        clientBackend = backend(clientTransport) {
                            auto()
                        }

                        testFun()

                        // FIXME figure out how to stop backends in tests properly
//                        clientBackend.stop()
//                        serverBackend.stop()
                    }
                }
            }
    }
}