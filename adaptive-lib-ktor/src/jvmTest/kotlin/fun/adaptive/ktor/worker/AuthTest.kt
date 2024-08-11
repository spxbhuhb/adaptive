/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.auth.model.AccessDenied
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.ktor.withProtoWebSocketTransport
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.lib.auth.crypto.BCrypt
import `fun`.adaptive.lib.auth.store.CredentialTable
import `fun`.adaptive.lib.auth.store.PrincipalTable
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.server.AdaptiveServerAdapter
import `fun`.adaptive.server.builtin.service
import `fun`.adaptive.server.server
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.UUID
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

            PrincipalTable += admin
            CredentialTable += Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now())
        }

        runBlocking {
            val transport = withProtoWebSocketTransport("http://localhost:8080")

            if (login) {
                getService<SessionApi>().login("admin", "stuff")
                delay(100) // let the websocket disconnect
            }
            test(adapter)

            transport.stop()
            adapter.stop()
        }
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
            delay(100) // let the websocket disconnect

            assertFailsWith(AccessDenied::class) {
                getService<AuthTestApi>().loggedInFun()
            }
        }
    }

}