/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.authJvm
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.backend
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallException
import `fun`.adaptive.service.transport.ServiceCallTransport
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

suspend fun checkNumber(transport: ServiceCallTransport, i: Int, illegal: Boolean): String {
    try {
        getService<NumberApi>(transport).ensureEven(i, illegal)
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
        test: suspend (it: BackendAdapter) -> Unit
    ) {
        val serverBackend = backend {
            inMemoryH2(callSiteName.substringAfterLast('.'))
            service { NumberService() }
            authJvm() // to have session worker
            ktor()
        }

        val clientBackend = backend(webSocketTransport("http://localhost:8080")) { }.start()

        runBlocking {
            try {
                test(clientBackend)
            } finally {
                clientBackend.stop()
                serverBackend.stop()
            }
        }
    }

    @Test
    fun throwAdat() {
        exceptionTest {
            assertEquals("this is an odd number", checkNumber(it.transport, 13, false))
        }
    }

    @Test
    fun throwNonAdat() {
        exceptionTest {
            assertEquals("ServiceCallException", checkNumber(it.transport, 13, true))
        }
    }

}