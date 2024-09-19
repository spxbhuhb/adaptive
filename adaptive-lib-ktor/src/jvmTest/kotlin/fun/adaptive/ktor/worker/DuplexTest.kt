/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.getService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals


@ServiceApi
interface DuplexApi {
    suspend fun process(value: String): String
}

class DuplexService : DuplexApi, ServiceImpl<DuplexService> {

    override suspend fun process(value: String): String {
        println("$this  ${serviceContext.transport}")
        if (value.length > 4) {
            return value
        } else {
            val side = if (adapter == null) "c" else "S"
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
        val serverBackend = backend {
            inMemoryH2(callSiteName.substringAfterLast('.'))
            service { DuplexService() }
            auth() // to have session worker
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
    fun throwAdat() {
        duplexTest {
            val service = getService<DuplexApi>(it.transport)

            val result = service.process("")

            assertEquals("ScScS", result)
        }
    }

}