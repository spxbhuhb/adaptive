package `fun`.adaptive.test

import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.test.TestClientApplication.Companion.testClient
import `fun`.adaptive.test.TestServerApplication.Companion.testServer
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueApi
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.app.ValueClientModule
import `fun`.adaptive.value.app.ValueServerModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Suppress("OPT_IN_USAGE")
class TestApplicationTest {

    @Test
    fun basic() {
        testServer {
            worker { AvValueWorker(proxy = false) }
        }.also {
            it.start()
            assertNotNull(it.backend.firstImpl<AvValueWorker>())
        }
    }

    @Test
    fun withModule() {
        testServer {
            module { ValueServerModule() }
        }.also {
            it.start()
            assertNotNull(it.backend.firstImpl<AvValueWorker>())
        }
    }

    @Test
    fun serverClient() = runTest {
        val server = testServer {
            module { ValueServerModule() }
        }

        val client = testClient(server) {
            backendModule { ValueClientModule() }
        }

        val serverWorker = server.backend.firstImpl<AvValueWorker>()
        val value = serverWorker.executeOutOfBand {
            addValue { AvValue(spec = "Hello World!") }
        }

        withContext(Dispatchers.Default.limitedParallelism(1)) {
            val valueService = getService<AvValueApi>(client.backend.transport)
            val readBack = valueService.get(value.uuid)

            assertEquals(value.spec, readBack?.spec)
        }
    }
}