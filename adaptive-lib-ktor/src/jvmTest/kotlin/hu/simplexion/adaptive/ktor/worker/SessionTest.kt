/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.auth.api.SessionApi
import hu.simplexion.adaptive.auth.model.*
import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.ClientWebSocketServiceCallTransport
import hu.simplexion.adaptive.ktor.WebSocketServiceCallTransport
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.ktor.withProtoWebSocketTransport
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.lib.auth.crypto.BCrypt
import hu.simplexion.adaptive.lib.auth.store.CredentialTable
import hu.simplexion.adaptive.lib.auth.store.PrincipalTable
import hu.simplexion.adaptive.reflect.CallSiteName
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.query.firstImpl
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.service.model.DisconnectException
import hu.simplexion.adaptive.utility.UUID
import io.ktor.client.plugins.cookies.*
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class SessionTest {

    lateinit var adapter: AdaptiveServerAdapter

    fun server(dbName: String) = server {
        adapter = it as AdaptiveServerAdapter
        inMemoryH2(dbName)
        auth() // to have session worker
        ktor()
    }

    val transport
        get() = (defaultServiceCallTransport as ClientWebSocketServiceCallTransport)

    fun WebSocketServiceCallTransport.stop() {
        scope.cancel()
        runBlocking {
            // this is not the perfect solution, but I don't know a better one
            // see: https://kotlinlang.slack.com/archives/C1CFAFJSK/p1721638059715979
            delay(100)
        }
    }

    @AfterTest
    fun cleanup() {
        transport.stop()
        adapter.stop()
    }

    @Test
    fun getSession() {
        server("getSession")
        val ktorWorker = adapter.firstImpl<KtorWorker>()

        runBlocking {
            withProtoWebSocketTransport("ws://localhost:8080/adaptive/service-ws", "http://localhost:8080/adaptive/client-id")

            val sessionService = getService<SessionApi>()

            val client = transport.client

            val cookies = client.cookies("http://localhost:8080")
            assertNotNull(cookies.firstOrNull { it.name == ktorWorker.clientIdCookieName })

            val session = sessionService.getSession()
            assertNull(session)
        }
    }

    @CallSiteName
    fun sessionTest(
        callSiteName: String = "unknown",
        test: suspend (it: AdaptiveServerAdapter) -> Unit
    ) {
        server(callSiteName.substringAfterLast('.'))

        runBlocking {
            withProtoWebSocketTransport("ws://localhost:8080/adaptive/service-ws", "http://localhost:8080/adaptive/client-id", true)

            test(adapter)
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

            sessionService.logout()

            assertFailsWith(DisconnectException::class) {
                sessionService.getSession()
            }
        }
    }
}