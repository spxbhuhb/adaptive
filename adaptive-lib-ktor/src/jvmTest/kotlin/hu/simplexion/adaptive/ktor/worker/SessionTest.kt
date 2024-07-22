/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.auth.api.SessionApi
import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.BasicWebSocketServiceCallTransport
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.ktor.withProtoWebSocketTransport
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.server.query.firstImpl
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SessionTest {

    fun server() = server {
        inMemoryH2()
        auth() // to have session worker
        ktor()
    }

    @Test
    fun getSession() {
        val adapter = server()
        val sessionWorker = adapter.firstImpl<SessionWorker>()

        withProtoWebSocketTransport("ws://localhost:8080/adaptive/service", true)

        val sessionService = getService<SessionApi>()

        val client = (defaultServiceCallTransport as BasicWebSocketServiceCallTransport).client

        runBlocking {
            val response = client.get("http://localhost:8080/adaptive/session-id")
            assertEquals(HttpStatusCode.OK, response.status)

            val cookies = client.cookies("http://localhost:8080")
            assertNotNull(cookies.firstOrNull { it.name == sessionWorker.sessionCookieName })

            val session = sessionService.getSession()
            assertNull(session)
        }

        adapter.stop()
    }

}