/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.ktor.withProtoWebSocketTransport
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.server.AdaptiveServerAdapter
import `fun`.adaptive.server.builtin.ServiceImpl
import `fun`.adaptive.server.builtin.service
import `fun`.adaptive.server.server
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallException
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

@Adat
class OddNumberException : Exception()

@ServiceApi
interface NumberApi {
    suspend fun ensureEven(i: Int, illegal: Boolean)
}

class NumberService : NumberApi, ServiceImpl<NumberService> {

    override suspend fun ensureEven(i: Int, illegal: Boolean) {
        publicAccess()
        if (i % 2 == 1) {
            if (illegal) throw IllegalArgumentException() else throw OddNumberException()
        }
    }

}

suspend fun checkNumber(i: Int, illegal: Boolean): String {
    try {
        getService<NumberApi>().ensureEven(i, illegal)
        return "this is an even number"
    } catch (_: OddNumberException) {
        return "this is an odd number"
    } catch (_: ServiceCallException) {
        return "ServiceCallException"
    }
}

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class ExceptionTest {

    @CallSiteName
    fun exceptionTest(
        callSiteName: String = "unknown",
        test: suspend (it: AdaptiveServerAdapter) -> Unit
    ) {
        val adapter = server {
            inMemoryH2(callSiteName.substringAfterLast('.'))
            service { NumberService() }
            auth() // to have session worker
            ktor()
        }

        runBlocking {
            val transport = withProtoWebSocketTransport("http://localhost:8080")

            try {
                test(adapter)
            } finally {
                transport.stop()
                adapter.stop()
            }
        }
    }

    @Test
    fun throwAdat() {
        exceptionTest {
            assertEquals("this is an odd number", checkNumber(13, false))
        }
    }

    @Test
    fun throwNonAdat() {
        exceptionTest {
            assertEquals("ServiceCallException", checkNumber(13, true))
        }
    }

}