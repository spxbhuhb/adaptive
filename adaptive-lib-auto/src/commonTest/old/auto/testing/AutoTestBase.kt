package `fun`.adaptive.auto.testing

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.seconds

class AutoTestBase {

    val clientTransport = DirectServiceTransport()
    val serverTransport = DirectServiceTransport()

    init {
        clientTransport.peerTransport = serverTransport
        serverTransport.peerTransport = clientTransport
    }

    lateinit var serverBackend: BackendAdapter

    lateinit var clientBackend: BackendAdapter

    suspend fun run(testMain: suspend () -> Unit) {
        testMain()
    }

    suspend fun waitForSync(a1: AutoInstance<*, *, *, *>, a2: AutoInstance<*, *, *, *>) {
        waitForReal(1.seconds) {
            a1.context.time.timestamp == a2.context.time.timestamp
        }

    }
    fun serverList() = serverBackend.firstImpl<AutoTestWorker>().list

    companion object {

        fun autoTest(testFun: suspend AutoTestBase.() -> Unit) = runTest {
            val base = AutoTestBase()
            base.testFun()
        }

        fun autoTestWorker(trace: Boolean = false, testFun: suspend AutoTestBase.() -> Unit) =
            runTest {
                autoTest {

                    serverBackend = backend(serverTransport) {
                        auto()
                        service { AutoTestServiceInWorker() }
                        worker { AutoTestWorker(trace) }
                    }

                    clientBackend = backend(clientTransport) {
                        auto()
                    }

                    run {
                        testFun()
                    }

                    clientBackend.stop()
                    serverBackend.stop()
                }
            }
    }
}