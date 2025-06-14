package `fun`.adaptive.value.remote

import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.lib.util.app.UtilModule
import `fun`.adaptive.test.TestClientApplication.Companion.testClient
import `fun`.adaptive.test.TestServerApplication.Companion.testServer
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.app.ValueClientModule
import `fun`.adaptive.value.app.ValueServerModule
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

class AvRemoteValueTest {

    @Test
    fun basic() = runTest {
        val valueId = AvValueId()

        val testServer = testServer {
            module { UtilModule() }
            module { ValueServerModule() }
        }

        val testClient = testClient(testServer) {
            module { ValueClientModule() }
        }

        val result = mutableListOf<String?>()

        test(backendAdapter = testClient.backend) {
            val value = avRemoteValue(valueId, String::class).also { result += it?.spec }
            println(value)
        }

        val serverValueWorker = testServer.backend.firstImpl<AvValueWorker>()
        serverValueWorker.executeOutOfBand {
            this += AvValue(valueId, spec = "Hello World!")
        }

        waitForReal(2.seconds) { result.count { it != null } == 1 }

        assertNull(result[0])
        assertEquals(result[1], "Hello World!")
    }
}