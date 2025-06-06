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
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.api.Proto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.plugins.origin
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.utils.io.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

class KtorWorker(
    port: Int = 8080
) : WorkerImpl<KtorWorker>() {

    val port by setting<Int> { "KTOR_PORT" } default port
    val wireFormat by setting<String> { "KTOR_WIREFORMAT" } default "json"
    val staticResourcesPath by setting<String> { "KTOR_STATIC_FILES" } default "./var/static"
    val downloadPath by setting<String> { "KTOR_DOWNLOAD_DIR" } default "./var/download"
    val serviceWebSocketRoute by setting<String> { "KTOR_SERVICE_WEBSOCKET_ROUTE" } default "/adaptive/service-ws"
    val clientIdRoute by setting<String> { "KTOR_CLIENT_ID_ROUTE" } default "/adaptive/client-id"
    val downloadRoute by setting<String> { "KTOR_DOWNLOAD_ROUTE" } default "/adaptive/download"
    val clientIdCookieName by setting<String> { "KTOR_SESSION_COOKIE_NAME" } default "ADAPTIVE_CLIENT_ID"

    val qualifiedCookieName
        get() = clientIdCookieName + "_$port"

    val sessionProvider by implOrNull<ServiceSessionProvider>()

    var applicationEngine: ApplicationEngine? = null

    val fileNameRegex = Regex("[\\w\\-. ]+")
    val fileTransport = KtorFileTransport(this)

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

        install(XForwardedHeaders)

        routing {
            clientId()
            sessionWebsocketServiceCallTransport()
            jsAppFiles()
            downloadFiles()
        }
    }

    fun Routing.clientId() {
        val provider = sessionProvider ?: return

        get(clientIdRoute) {
            val existingClientId = call.request.cookies[qualifiedCookieName]?.let { UUID<ServiceContext>(it) }

            val id = if (existingClientId != null && provider.getSession(existingClientId) != null) {
                existingClientId
            } else {
                UUID()
            }.toString()

            call.response.cookies.append(qualifiedCookieName, id, httpOnly = true, path = "/")
            call.respond(HttpStatusCode.OK)
        }
    }

    fun Routing.sessionWebsocketServiceCallTransport() {
        val safeAdapter = adapter ?: return
        val provider = sessionProvider

        if (sessionProvider == null) {
            logger.warning("No session provider, authentication won't work.")
        }

        webSocket(serviceWebSocketRoute) {

            val sessionUuid = call.request.cookies[qualifiedCookieName]?.let { UUID<ServiceContext>(it) } ?: UUID()

            logger.info {
                with(call.request.origin) { "WS-CONNECT $remoteAddress:$remotePort $sessionUuid" }
            }

            val wireFormatProvider =
                when (wireFormat.lowercase()) {
                    "proto" -> Proto
                    "json" -> Json
                    else -> throw IllegalArgumentException("invalid wire format: $wireFormat, expected proto or json")
                }

            val transport = ServerWebSocketServiceCallTransport(
                wireFormatProvider,
                this,
                sessionUuid,
                provider?.getSession(sessionUuid),
                fileTransport
            )

            transport.serviceImplFactory = safeAdapter
            transport.start()

            try {

                transport.incoming()

            } catch (_: CancellationException) {
                // shutdown, no error to be logged there
                currentCoroutineContext().ensureActive()
            } catch (ex: Exception) {
                transport.transportLog.error(ex)
            } finally {
                safeSuspendCall(logger) {
                    transport.disconnect()
                }
                logger.info { with(call.request.origin) { "WS-DISCONNECT $remoteAddress:$remotePort $sessionUuid" } }
            }

        }

    }

    fun Routing.jsAppFiles() {
        staticFiles("/", File(staticResourcesPath)) {
            this.default("index.html")
            preCompressed(CompressedFileType.BROTLI, CompressedFileType.GZIP)
        }
    }

    fun Routing.downloadFiles() {
        val provider = sessionProvider ?: return

        get("$downloadRoute/{fileName}") {
            val clientId = call.request.cookies[qualifiedCookieName]?.let { UUID<ServiceContext>(it) }
            if (clientId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val session = provider.getSession(clientId)
            if (session == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val fileName = call.parameters["fileName"]
            if (fileName == null || ! fileName.matches(fileNameRegex)) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val file = File(fileTransport.getDownloadPath(clientId, fileName).toString())

            if (! file.exists()) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            logger.info { "DOWNLOAD-START  ${session.uuid} $file" }

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, fileName).toString()
            )

            try {
                call.respondOutputStream(ContentType.defaultForFile(file)) {
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(this)
                    }
                }
                logger.info { "DOWNLOAD-SUCCESS ${session.uuid} $file" }
            } catch (ex : Exception) {
                logger.info("DOWNLOAD-FAIL ${session.uuid} $file", ex)
            } finally {
                file.delete()
            }
        }
    }

}