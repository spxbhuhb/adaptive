/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.auth.api.SessionApi
import hu.simplexion.adaptive.auth.model.*
import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.ktor.withJsonWebSocketTransport
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.lib.auth.crypto.BCrypt
import hu.simplexion.adaptive.reflect.CallSiteName
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.query.firstImpl
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.server.setting.dsl.inline
import hu.simplexion.adaptive.server.setting.dsl.settings
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.utility.UUID
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

    fun server(dbName: String) = server {
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
            val transport = withJsonWebSocketTransport("ws://localhost:8080/adaptive/service-ws", "http://localhost:8080/adaptive/client-id")

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
        test: suspend (it: AdaptiveServerAdapter) -> Unit
    ) {
        val adapter = server(callSiteName.substringAfterLast('.'))

        runBlocking {
            val transport = withJsonWebSocketTransport("ws://localhost:8080/adaptive/service-ws", "http://localhost:8080/adaptive/client-id", true)

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