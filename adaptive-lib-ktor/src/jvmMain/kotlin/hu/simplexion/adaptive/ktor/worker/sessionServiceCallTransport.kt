/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.transport.ServiceSessionProvider
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.cancellation.CancellationException

fun Routing.sessionWebsocketServiceCallTransport(
    useTextFrame: Boolean,
    wireFormatProvider: WireFormatProvider,
    adapter: AdaptiveServerAdapter,
    sessionProvider: ServiceSessionProvider,
    path: String = "/adaptive/service"
) {
    webSocket(path) {

        val sessionUuid = call.request.cookies[sessionProvider.getKey()]?.let { UUID<ServiceContext>(it) } ?: UUID()

        val transport = TransactionWebSocketServiceCallTransport(
            useTextFrame,
            wireFormatProvider,
            adapter,
            this,
            sessionProvider.getSession(sessionUuid)
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