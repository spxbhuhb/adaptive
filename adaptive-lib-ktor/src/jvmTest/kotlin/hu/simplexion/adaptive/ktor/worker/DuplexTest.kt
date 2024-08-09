/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.ktor.withProtoWebSocketTransport
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.reflect.CallSiteName
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.service.getService
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
        test: suspend (it: AdaptiveServerAdapter) -> Unit
    ) {
        val adapter = server {
            inMemoryH2(callSiteName.substringAfterLast('.'))
            service { DuplexService() } // this is the server side service
            auth() // to have session worker
            ktor()
        }

        defaultServiceImplFactory += DuplexService() // this is the client side service

        runBlocking {
            val transport = withProtoWebSocketTransport("ws://localhost:8080/adaptive/service-ws", "http://localhost:8080/adaptive/client-id", true)

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