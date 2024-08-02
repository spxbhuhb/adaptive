/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auth.context.publicAccess
import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.WebSocketServiceCallTransport
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.ktor.withProtoWebSocketTransport
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.reflect.CallSiteName
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.service.transport.ServiceCallException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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
    } catch (ex: OddNumberException) {
        return "this is an odd number"
    } catch (ex: ServiceCallException) {
        return "ServiceCallException"
    }
}

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class ExceptionTest {

    val transport
        get() = (defaultServiceCallTransport as WebSocketServiceCallTransport)

    fun WebSocketServiceCallTransport.stop() {
        scope.cancel()
        runBlocking {
            // this is not the perfect solution, but I don't know a better one
            // see: https://kotlinlang.slack.com/archives/C1CFAFJSK/p1721638059715979
            delay(100)
        }
    }

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
            withProtoWebSocketTransport("ws://localhost:8080/adaptive/service", "http://localhost:8080/adaptive/client-id")

            test(adapter)
        }

        transport.stop()
        adapter.stop()
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