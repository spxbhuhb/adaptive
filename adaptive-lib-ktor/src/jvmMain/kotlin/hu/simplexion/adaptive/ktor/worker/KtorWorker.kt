/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.server.builtin.WorkerImpl
import hu.simplexion.adaptive.server.builtin.implOrNull
import hu.simplexion.adaptive.server.setting.dsl.setting
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.transport.ServiceSessionProvider
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
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
    val wireFormat by setting<String> { "KTOR_WIREFORMAT" } default "proto"
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
        applicationEngine?.stop(0, 0, TimeUnit.MILLISECONDS)
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
                    "proto" -> ProtoWireFormatProvider()
                    "json" -> JsonWireFormatProvider()
                    else -> throw IllegalArgumentException("invalid wire format: $wireFormat, expected proto or json")
                }

            val transport = TransactionWebSocketServiceCallTransport(
                wireFormatProvider,
                safeAdapter,
                this,
                provider.getSession(sessionUuid)
            )

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