/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.implOrNull
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.transport.ServiceSessionProvider
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.api.Proto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.utils.io.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

class KtorWorker : WorkerImpl<KtorWorker> {

    val port by setting<Int> { "KTOR_PORT" } default 8080
    val wireFormat by setting<String> { "KTOR_WIREFORMAT" } default "json"
    val staticResourcesPath by setting<String> { "KTOR_STATIC_FILES" } default "./var/static"
    val serviceWebSocketRoute by setting<String> { "KTOR_SERVICE_WEBSOCKET_ROUTE" } default "/adaptive/service-ws"
    val clientIdRoute by setting<String> { "KTOR_CLIENT_ID_ROUTE" } default "/adaptive/client-id"
    val clientIdCookieName by setting<String> { "KTOR_SESSION_COOKIE_NAME" } default "ADAPTIVE_CLIENT_ID"

    val sessionProvider by implOrNull<ServiceSessionProvider>()

    var applicationEngine: ApplicationEngine? = null

    override fun mount() {
        embeddedServer(Netty, port = port, module = { module() }).also {
            applicationEngine = it
            it.start(wait = false) // FIXME think about Ktor server wait parameter
        }
    }

    override suspend fun run() {
        // nothing to do here, mount starts the application engine
    }

    override fun unmount() {
        applicationEngine?.stop(1000, 1000, TimeUnit.MILLISECONDS)
    }

    fun Application.module() {

        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(20)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }

        routing {
            clientId()
            sessionWebsocketServiceCallTransport()

            staticFiles("/", File(staticResourcesPath)) {
                this.default("index.html")
            }
        }

    }

    fun Routing.sessionWebsocketServiceCallTransport() {
        val safeAdapter = adapter ?: return
        val provider = sessionProvider ?: return

        webSocket(serviceWebSocketRoute) {

            val sessionUuid = call.request.cookies[clientIdCookieName]?.let { UUID<ServiceContext>(it) } ?: UUID()

            val wireFormatProvider =
                when (wireFormat.lowercase()) {
                    "proto" -> Proto
                    "json" -> Json
                    else -> throw IllegalArgumentException("invalid wire format: $wireFormat, expected proto or json")
                }

            val transport = TransactionWebSocketServiceCallTransport(
                wireFormatProvider,
                this,
                sessionUuid,
                provider.getSession(sessionUuid)
            )

            transport.start(safeAdapter)

            try {

                transport.incoming()

            } catch (_: CancellationException) {
                // shutdown, no error to be logged there
                currentCoroutineContext().ensureActive()
            } catch (ex: Exception) {
                transport.transportLog.error(ex)
            }

        }

    }

    fun Routing.clientId() {
        val provider = sessionProvider ?: return

        get(clientIdRoute) {
            val existingClientId = call.request.cookies[clientIdCookieName]?.let { UUID<ServiceContext>(it) }

            val id = if (existingClientId != null && provider.getSession(existingClientId) != null) {
                existingClientId
            } else {
                UUID()
            }.toString()

            call.response.cookies.append(clientIdCookieName, id, httpOnly = true, path = "/")
            call.respond(HttpStatusCode.OK)
        }
    }
}