package `fun`.adaptive.test

import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.test.TestServerApplication.Companion.testServer
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.app.ValueServerModule
import kotlin.test.Test
import kotlin.test.assertNotNull

class TestServerApplicationTest {

    @Test
    fun basic() {
        testServer {
            adhoc { AvValueWorker(proxy = false) }
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
}