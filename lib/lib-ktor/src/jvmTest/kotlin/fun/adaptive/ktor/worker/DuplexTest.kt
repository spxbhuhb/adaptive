/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.api.getService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals


@ServiceApi
interface DuplexApi {
    suspend fun process(value: String): String
}

class DuplexService : DuplexApi, ServiceImpl<DuplexService>() {

    override suspend fun process(value: String): String {
        if (value.length > 4) {
            return value
        } else {
            val side = if (value.length % 2 == 0) "S" else "c"
            return getService<DuplexApi>(serviceContext.transport).process(value + side)
        }
    }

}

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class DuplexTest {

    @CallSiteName
    fun duplexTest(
        callSiteName: String = "unknown",
        test: suspend (it: BackendAdapter) -> Unit
    ) {
        // Here the server backend does not need a specific transport. DuplexService
        // uses the transport from the service context which is created when the
        // client connects though websocket.

        val serverBackend = backend {
            service { DuplexService() }
            ktor()
        }

        val clientBackend = backend(webSocketTransport("http://localhost:8080")) {
            service { DuplexService() }
        }.start()

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
    fun duplexTest() {
        duplexTest {
            // it.transport is a ClientWebSocketServiceCallTransport
            val service = getService<DuplexApi>(it.transport)

            val result = service.process("")

            assertEquals("ScScS", result)
        }
    }

}