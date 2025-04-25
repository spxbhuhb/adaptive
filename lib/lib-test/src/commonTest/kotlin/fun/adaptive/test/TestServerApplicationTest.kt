package `fun`.adaptive.test

import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.test.TestServerApplication.Companion.testServer
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.app.ValueServerModule
import kotlin.test.Test
import kotlin.test.assertEquals

class TestServerApplicationTest {

    @Test
    fun basic() {
        testServer {
            adhoc { AvValueWorker("test") }
        }.also {
            assertEquals("test", it.backend.firstImpl<AvValueWorker>().domain)
        }
    }

    @Test
    fun withModule() {
        testServer {
            module { ValueServerModule("test", { }) }
        }.also {
            assertEquals("test", it.backend.firstImpl<AvValueWorker>().domain)
        }
    }
}