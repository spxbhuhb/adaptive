/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.auth.api.SessionApi
import hu.simplexion.adaptive.auth.model.AccessDenied
import hu.simplexion.adaptive.auth.model.Credential
import hu.simplexion.adaptive.auth.model.CredentialType
import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.BasicWebSocketServiceCallTransport
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.ktor.withProtoWebSocketTransport
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.lib.auth.crypto.BCrypt
import hu.simplexion.adaptive.lib.auth.store.CredentialTable
import hu.simplexion.adaptive.lib.auth.store.PrincipalTable
import hu.simplexion.adaptive.reflect.CallSiteName
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.utility.UUID
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class AuthTest {

    val transport
        get() = (defaultServiceCallTransport as BasicWebSocketServiceCallTransport)

    fun BasicWebSocketServiceCallTransport.stop() {
        scope.cancel()
        runBlocking {
            // this is not the perfect solution, but I don't know a better one
            // see: https://kotlinlang.slack.com/archives/C1CFAFJSK/p1721638059715979
            delay(100)
        }
    }

    @CallSiteName
    fun authTest(
        callSiteName: String = "unknown",
        login: Boolean = true,
        test: suspend (it: AdaptiveServerAdapter) -> Unit
    ) {
        val adapter = server {
            inMemoryH2(callSiteName.substringAfterLast('.'))
            service { AuthTestService() }
            auth() // to have session worker
            ktor()
        }

        transaction {
            val admin = Principal(UUID(), "admin", activated = true)
            val passwd = BCrypt.hashpw("stuff", BCrypt.gensalt())

            transaction {
                PrincipalTable += admin
                CredentialTable += Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now())
            }
        }

        runBlocking {
            withProtoWebSocketTransport("ws://localhost:8080/adaptive/service", "http://localhost:8080/adaptive/client-id")
            if (login) getService<SessionApi>().login("admin", "stuff")
            test(adapter)
        }

        transport.stop()
        adapter.stop()
    }

    @Test
    fun publicAccessNotLoggedIn() {
        authTest(login = false) {
            val result = getService<AuthTestApi>().publicFun()
            assertEquals("publicFun", result)
        }
    }

    @Test
    fun publicAccessLoggedIn() {
        authTest {
            val result = getService<AuthTestApi>().publicFun()
            assertEquals("publicFun", result)
        }
    }

    @Test
    fun loggedInNotLoggedIn() {
        authTest(login = false) {
            assertFailsWith(AccessDenied::class) {
                getService<AuthTestApi>().loggedInFun()
            }
        }
    }

    @Test
    fun loggedInLoggedIn() {
        authTest(login = true) {
            val result = getService<AuthTestApi>().loggedInFun()
            assertEquals("loggedInFun", result)
        }
    }

    @Test
    fun loggedInAfterLogout() {
        authTest(login = true) {
            getService<SessionApi>().logout()
            assertFailsWith(AccessDenied::class) {
                getService<AuthTestApi>().loggedInFun()
            }
        }
    }

}