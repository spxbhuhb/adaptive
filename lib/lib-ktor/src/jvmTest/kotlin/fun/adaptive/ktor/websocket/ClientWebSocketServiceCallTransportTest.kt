/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.websocket

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.testPort
import `fun`.adaptive.ktor.worker.KtorWorker
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.ServiceProvider
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.model.DisconnectException
import `fun`.adaptive.service.transport.DelayReconnectException
import `fun`.adaptive.service.transport.ServiceCallException
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.waitForReal
import io.ktor.websocket.close
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

@Adat
class OddNumberException : Exception()

@ServiceApi
interface NumberApi {
    suspend fun increment(i: Int): Int
    suspend fun ensureEven(i: Int, illegal: Boolean)
    suspend fun store(i: Int, name: String)
    suspend fun retrieve(name: String): Int
    suspend fun disconnect()
}

val lock = getLock()
var storedNumbers = mutableMapOf<String, Int>()

fun directGet(name: String) =
    lock.use {
        storedNumbers[name] ?: 12
    }

fun directSet(v: Int, name: String) =
    lock.use {
        storedNumbers[name] = v
    }

@ServiceProvider
class NumberService : NumberApi, ServiceImpl<NumberService>() {

    override suspend fun increment(i: Int): Int {
        return i + 1
    }

    override suspend fun ensureEven(i: Int, illegal: Boolean) {
        publicAccess()
        if (i % 2 == 1) {
            if (illegal) throw IllegalArgumentException() else throw OddNumberException()
        }
    }

    override suspend fun store(i: Int, name: String) {
        lock.use {
            storedNumbers[name] = i
        }
    }

    override suspend fun retrieve(name: String): Int =
        lock.use {
            storedNumbers[name] ?: 12
        }

    override suspend fun disconnect() {
        (serviceContext.transport as WebSocketServiceCallTransport).also {
            it.socketLock.use {
                it.socket?.close()
            }
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


class ClientWebSocketServiceCallTransportTest {

    @CallSiteName
    fun test(
        callSiteName: String = "<unknown>",
        port: Int = testPort,
        trace: Boolean = false,
        setupFun: suspend (ServiceCallTransport) -> Unit = { },
        testFun: suspend (it: BackendAdapter) -> Unit
    ) {

        val serverBackend = backend {
            service { NumberService() }
            worker { KtorWorker(port) }
        }.also {
            if (trace) {
                it.transport.trace = true
                it.transport.transportLog.enableFine()
            }
        }

        val clientTransport = webSocketTransport("http://127.0.0.1:$port", setupFun = setupFun)
            .also {
                if (trace) {
                    it.trace = true
                    it.transportLog.enableFine()
                }
            }

        val clientBackend = backend(clientTransport) { }

        runBlocking {
            try {
                testFun(clientBackend)
            } finally {
                clientBackend.stop()
                serverBackend.stop()
            }
        }
    }

    @Test
    fun `basic connection`() = test {
        val numberService = getService<NumberApi>(it.transport)
        assertEquals(2, numberService.increment(1))
    }

    @Test
    fun `exception test`() = test {
        assertEquals("this is an odd number", checkNumber(it.transport, 13, false))
    }

    @Test
    fun `illegal argument test`() = test {
        assertEquals("ServiceCallException", checkNumber(it.transport, 13, true))
    }

    @Test
    fun `reconnect test`() = test {
        val service = getService<NumberApi>(it.transport)

        assertFailsWith<DisconnectException> { service.disconnect() }

        assertEquals(2, service.increment(1))
    }

    @Test
    fun `reconnect test with setup fun`() = test(
        trace = true,
        setupFun = { getService<NumberApi>(it).store(23, "reconnect test with setup fun") }
    ) {
        val service = getService<NumberApi>(it.transport)

        assertFailsWith<DisconnectException> { service.disconnect() }

        waitForReal(1.seconds) { directGet("reconnect test with setup fun") == 23 }

        assertEquals(23, service.retrieve("reconnect test with setup fun"))
    }

    @Test
    fun `reconnect test with setup fun failed`() = test(
        trace = true,
        setupFun = {
            if (directGet("reconnect test with setup fun failed") == 12) {
                directSet(23, "reconnect test with setup fun failed")
                throw DelayReconnectException(200.milliseconds)
            }
        }
    ) {
        waitForReal(10.seconds) { directGet("reconnect test with setup fun failed") == 23 }

        val service = getService<NumberApi>(it.transport)

        assertEquals(23, service.retrieve("reconnect test with setup fun failed"))
    }

    val longTestRepeatCount = 100 // increment this for really long running tests

    @Test
    fun `reconnect test with setup fun failed long running`() = test(
        setupFun = {
            val v = directGet("reconnect test with setup fun failed long running")
            if (v < longTestRepeatCount) {
                if (v % 100 == 0) println("storedNumber: $v")
                directSet(v + 1, "reconnect test with setup fun failed long running")
                throw DelayReconnectException(1.milliseconds)
            }
        }
    ) {
        waitForReal(60.seconds) { directGet("reconnect test with setup fun failed long running") == longTestRepeatCount }

        val service = getService<NumberApi>(it.transport)

        assertEquals(longTestRepeatCount, service.retrieve("reconnect test with setup fun failed long running"))
    }

    @Test
    fun `many service calls, single thread`() = test { adapter ->
        val service = getService<NumberApi>(adapter.transport)
        measureTime {
            repeat(longTestRepeatCount) {
                assertEquals(it + 1, service.increment(it))
            }
        }.also { if (it.inWholeSeconds > 0) println("total time: $it request/second: ${(longTestRepeatCount / it.inWholeMilliseconds.toDouble()) * 1000} time/request: ${it / longTestRepeatCount.toDouble()}") }
    }

    @Test
    fun `many service calls, multi thread`() = test { adapter ->
        val service = getService<NumberApi>(adapter.transport)

        measureTime {
            runBlocking {
                launch { repeat(longTestRepeatCount / 4) { service.increment(it) } }
                launch { repeat(longTestRepeatCount / 4) { service.increment(it) } }
                launch { repeat(longTestRepeatCount / 4) { service.increment(it) } }
                launch { repeat(longTestRepeatCount / 4) { service.increment(it) } }
            }
        }.also { if (it.inWholeSeconds > 0) println("total time: $it request/second: ${(longTestRepeatCount / it.inWholeMilliseconds.toDouble()) * 1000}") }
        // total time: 35.884964083s request/second: 28571 time/request: 35.884us
    }

    @Test
    fun `many service calls, multi thread, locking`() = test { adapter ->
        val service = getService<NumberApi>(adapter.transport)

        measureTime {
            runBlocking {
                launch { repeat(longTestRepeatCount / 4) { service.retrieve("many service calls, multi thread, locking") } }
                launch { repeat(longTestRepeatCount / 4) { service.retrieve("many service calls, multi thread, locking") } }
                launch { repeat(longTestRepeatCount / 4) { service.retrieve("many service calls, multi thread, locking") } }
                launch { repeat(longTestRepeatCount / 4) { service.retrieve("many service calls, multi thread, locking") } }
            }
        }.also { if (it.inWholeSeconds > 0) println("total time: $it request/second: ${(longTestRepeatCount / it.inWholeMilliseconds.toDouble()) * 1000}") }
    }

}