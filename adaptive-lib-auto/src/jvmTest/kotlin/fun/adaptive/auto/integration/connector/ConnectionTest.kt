package `fun`.adaptive.auto.integration.connector

import `fun`.adaptive.auto.api.InstanceBase
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.waitFor
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ConnectionTest {

    @Test
    fun serverStop() {
        test { server, clientInstance, _ ->
            server.stop()

            clientInstance.frontend.update(DataItem("changed"))

            waitFor(3.seconds) {
                clientInstance.backend.context.connectors.isEmpty()
            }
        }
    }

    @Test
    fun clientStop() {
        test { server, clientInstance, transport ->

            transport.stop()

            val serverInstance = server.firstImpl<DataWorker>().masterData
            serverInstance.frontend.update(DataItem("changed"))

            delay(3000)

            assertTrue(serverInstance.backend.context.connectors.isEmpty())
        }
    }

    fun test(
        testFun: suspend (server: BackendAdapter, clientInstance: InstanceBase<DataItem>, transport: ServiceCallTransport) -> Unit
    ) {
        runBlocking {
            val server = backend {
                inMemoryH2("test")
                auth()
                ktor(port = 8088)

                auto()

                service { DataService() }
                worker { DataWorker(true) }
            }

            val transport = webSocketTransport("http://localhost:8088").also {
                it.responseTimeout = 1.seconds
            }

            try {

                val client = backend(transport = transport) {
                    auto()
                }

                val clientAutoWorker = client.firstImpl<AutoWorker>()

                val dataService = getService<DataServiceApi>(transport)

                val connectInfo = dataService.getConnectInfo()

                val clientInstance = autoInstance(clientAutoWorker, DataItem, handle = connectInfo.connectingHandle, trace = true)
                    .connect(transport = transport) { connectInfo }

                waitFor(2.seconds) { clientInstance.frontend.valueOrNull != null }

                testFun(server, clientInstance, transport)

            } finally {
                transport.stop()
                server.stop()
            }
        }
    }
}