/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.model.AccessDenied
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.authJvm
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.lib.auth.store.CredentialTable
import `fun`.adaptive.lib.auth.store.PrincipalTable
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.service.api.getService
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
        test: suspend (it: BackendAdapter) -> Unit
    ) {
        val serverBackend = backend {
            inMemoryH2(callSiteName.substringAfterLast('.'))
            service { AuthTestService() }
            authJvm() // to have session worker
            ktor()
        }

        val clientBackend = backend(webSocketTransport("http://localhost:8080")) { }.start()

        transaction {
            val admin = Principal(UUID(), "admin", activated = true)
            val passwd = BCrypt.hashpw("stuff", BCrypt.gensalt())

            PrincipalTable.plusAssign(admin)
            CredentialTable.plusAssign(Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now()))
        }

        runBlocking {
            if (login) {
                getService<AuthSessionApi>(clientBackend.transport).login("admin", "stuff")
                delay(100) // let the websocket disconnect
            }

            test(clientBackend)

            clientBackend.stop()
            serverBackend.stop()
        }
    }

    @Test
    fun publicAccessNotLoggedIn() {
        authTest(login = false) {
            val result = getService<AuthTestApi>(it.transport).publicFun()
            assertEquals("publicFun", result)
        }
    }

    @Test
    fun publicAccessLoggedIn() {
        authTest {
            val result = getService<AuthTestApi>(it.transport).publicFun()
            assertEquals("publicFun", result)
        }
    }

    @Test
    fun loggedInNotLoggedIn() {
        authTest(login = false) {
            assertFailsWith(AccessDenied::class) {
                getService<AuthTestApi>(it.transport).loggedInFun()
            }
        }
    }

    @Test
    fun loggedInLoggedIn() {
        authTest(login = true) {
            val result = getService<AuthTestApi>(it.transport).loggedInFun()
            assertEquals("loggedInFun", result)
        }
    }

    @Test
    fun loggedInAfterLogout() {
        authTest(login = true) {
            getService<AuthSessionApi>(it.transport).logout()
            delay(100) // let the websocket disconnect

            assertFailsWith(AccessDenied::class) {
                getService<AuthTestApi>(it.transport).loggedInFun()
            }
        }
    }

}