/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.ktor.api.withWebSocketTransport
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.lib.auth.crypto.BCrypt
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.lib.auth.store.CredentialTable
import `fun`.adaptive.lib.auth.store.PrincipalTable
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.UUID
import io.ktor.client.plugins.cookies.*
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class SessionTest {

    fun server(dbName: String) = backend {
        settings {
            inline("KTOR_WIREFORMAT" to "json")
        }
        inMemoryH2(dbName)
        auth() // to have session worker
        ktor()
    }

    @Test
    fun getSession() {
        val adapter = server("getSession")
        val ktorWorker = adapter.firstImpl<KtorWorker>()

        runBlocking {
            val transport = withWebSocketTransport("http://localhost:8080")

            try {
                val sessionService = getService<SessionApi>()

                val client = transport.client

                val cookies = client.cookies("http://localhost:8080")
                assertNotNull(cookies.firstOrNull { it.name == ktorWorker.clientIdCookieName })

                val session = sessionService.getSession()
                assertNull(session)
            } finally {
                transport.stop()
                adapter.stop()
            }
        }
    }

    @CallSiteName
    fun sessionTest(
        callSiteName: String = "unknown",
        test: suspend (it: BackendAdapter) -> Unit
    ) {
        val adapter = server(callSiteName.substringAfterLast('.'))

        runBlocking {
            val transport = withWebSocketTransport("http://localhost:8080")

            try {
                test(adapter)
            } finally {
                transport.stop()
                adapter.stop()
            }
        }
    }

    @Test
    fun loginUnknownPrincipal() {
        sessionTest {
            assertFailsWith(AuthenticationFail::class) {
                getService<SessionApi>().login("admin", "admin")
            }.also {
                assertEquals(AuthenticationResult.UnknownPrincipal, it.result)
            }
        }
    }

    @Test
    fun loginNoCredentials() {
        sessionTest {
            assertFailsWith(AuthenticationFail::class) {
                transaction {
                    PrincipalTable += Principal(UUID(), "admin")
                }
                getService<SessionApi>().login("admin", "admin")
            }.also {
                assertEquals(AuthenticationResult.NoCredential, it.result)
            }
        }
    }

    @Test
    fun loginNotActivated() {
        sessionTest {
            assertFailsWith(AuthenticationFail::class) {
                transaction {
                    val admin = Principal(UUID(), "admin")
                    val passwd = BCrypt.hashpw("stuff", BCrypt.gensalt())

                    PrincipalTable += admin
                    CredentialTable += Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now())
                }
                getService<SessionApi>().login("admin", "admin")
            }.also {
                assertEquals(AuthenticationResult.NotActivated, it.result)
            }
        }
    }

    @Test
    fun loginInvalidCredentials() {
        sessionTest {
            assertFailsWith(AuthenticationFail::class) {
                transaction {
                    val admin = Principal(UUID(), "admin", activated = true)
                    val passwd = BCrypt.hashpw("stuff", BCrypt.gensalt())

                    PrincipalTable += admin
                    CredentialTable += Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now())
                }
                getService<SessionApi>().login("admin", "other stuff")
            }.also {
                assertEquals(AuthenticationResult.InvalidCredentials, it.result)
            }
        }
    }

    @Test
    fun loginSuccess() {
        sessionTest {
            val admin = Principal(UUID(), "admin", activated = true)
            val passwd = BCrypt.hashpw("stuff", BCrypt.gensalt())

            transaction {
                PrincipalTable += admin
                CredentialTable += Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now())
            }

            val session = getService<SessionApi>().login("admin", "stuff")

            assertNotNull(session)
            assertEquals(admin.id, session.principalOrNull)
        }
    }

    @Test
    fun logoutSuccess() {
        sessionTest {
            val admin = Principal(UUID(), "admin", activated = true)
            val passwd = BCrypt.hashpw("stuff", BCrypt.gensalt())

            transaction {
                PrincipalTable += admin
                CredentialTable += Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now())
            }

            val sessionService = getService<SessionApi>()

            sessionService.login("admin", "stuff")
            delay(100) // to let the websocket close

            sessionService.logout()
            delay(100) // to let the websocket close

            sessionService.getSession()
        }
    }
}