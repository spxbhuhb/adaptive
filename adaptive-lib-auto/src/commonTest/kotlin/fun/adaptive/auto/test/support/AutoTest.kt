package `fun`.adaptive.auto.test.support

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.service.testing.DirectServiceTransport
import kotlinx.coroutines.test.runTest

class AutoTest {

    val clientTransport = DirectServiceTransport()
    val serverTransport = DirectServiceTransport()

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

                    serverBackend = backend(serverTransport) {
                        auto()
                    }

                    clientBackend = backend(clientTransport) {
                        auto()
                    }

                    testFun()

                    clientBackend.stop()
                    serverBackend.stop()
                }
            }
    }
}