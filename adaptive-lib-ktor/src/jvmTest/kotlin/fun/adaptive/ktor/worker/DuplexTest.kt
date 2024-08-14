/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.ktor.withProtoWebSocketTransport
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.backend
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.defaultServiceImplFactory
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
        val adapter = backend {
            inMemoryH2(callSiteName.substringAfterLast('.'))
            service { DuplexService() } // this is the server side service
            auth() // to have session worker
            ktor()
        }

        defaultServiceImplFactory += DuplexService() // this is the client side service

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
        duplexTest {
            val service = getService<DuplexApi>()

            val result = service.process("")

            assertEquals("ScScS", result)
        }
    }

}