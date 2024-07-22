/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.server.builtin.WorkerImpl
import hu.simplexion.adaptive.server.builtin.workerOrNull
import hu.simplexion.adaptive.server.setting.dsl.setting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

class KtorWorker : WorkerImpl<KtorWorker> {

    val port by setting<Int> { "KTOR_PORT" } default 8080
    val staticResourcesPath by setting<String> { "KTOR_STATIC" } default "./var/static"
    val serviceWebSocketRoute by setting<String> { "ADAPTIVE_SERVICE" } default "/adaptive/service"

    val sessionWorker by workerOrNull<SessionWorker>()

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
            sessionWorker?.let {
                clientId(it)
                sessionWebsocketServiceCallTransport(it, serviceWebSocketRoute)
            }
            staticFiles("/", File(staticResourcesPath)) {
                this.default("index.html")
            }
        }

    }
}