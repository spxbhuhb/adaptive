/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.server.builtin.WorkerImpl
import hu.simplexion.adaptive.server.setting.dsl.setting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.io.File
import java.time.Duration

class KtorWorker : WorkerImpl<KtorWorker> {

    val port by setting<Int> { "KTOR_PORT" }
    val static by setting<String> { "KTOR_STATIC" }

    lateinit var applicationEngine: ApplicationEngine

    override suspend fun run() {
        embeddedServer(Netty, port = port, module = { module() }).also {
            applicationEngine = it
            it.start(wait = false)
        }
    }

    fun Application.module() {

        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(20)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }

        routing {
            session()
            sessionWebsocketServiceCallTransport(this@KtorWorker)
            staticFiles("/", File(static)) {
                this.default("index.html")
            }
        }

    }
}