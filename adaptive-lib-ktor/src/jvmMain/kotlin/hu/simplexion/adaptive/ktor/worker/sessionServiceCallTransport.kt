/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.auth.context.getPrincipalOrNull
import hu.simplexion.adaptive.auth.model.Session.Companion.LOGOUT_TOKEN
import hu.simplexion.adaptive.ktor.ServerWebSocketServiceCallTransport
import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.log.AdaptiveLogger
import hu.simplexion.adaptive.log.getLogger
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.model.*
import hu.simplexion.adaptive.service.transport.ServiceSessionProvider
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*


// FIXME flood detection, session id brute force attack detection
fun Routing.sessionWebsocketServiceCallTransport(
    useTextFrame: Boolean,
    wireFormatProvider: WireFormatProvider,
    sessionProvider: ServiceSessionProvider<*>,
    path: String = "/adaptive/service"
) {
    webSocket(path) {

        val logger = getLogger("hu.simplexion.adaptive.ktor.worker.sessionWebsocketServiceCallTransport")
        logger.info("connection opened from ${call.request.local.remoteAddress}:${call.request.local.remotePort}")

        try {

            val sessionUuid = call.request.cookies[sessionProvider.getKey()]?.let { UUID<ServiceContext>(it) } ?: UUID()

            val transport = ServerWebSocketServiceCallTransport(
                useTextFrame,
                wireFormatProvider,
                this
            ) {
                sessionProvider.getSession(sessionUuid)
            }

            transport.incoming()

        } catch (_: kotlinx.coroutines.CancellationException) {
            // this is shutdown, no error to be logged there
        } catch (ex: Exception) {
            logger.error(ex)
        }
    }

}

suspend fun DefaultWebSocketSession.serve(
    logger: AdaptiveLogger,
    sessionWorker: SessionWorker,
    context: ServiceContext,
    requestEnvelope: RequestEnvelope
) {
    val responseEnvelope = try {


        ResponseEnvelope(
            requestEnvelope.callId,
            if (context.data[LOGOUT_TOKEN] == null) ServiceCallStatus.Ok else ServiceCallStatus.Logout,
            responsePayload
        )

    } catch (ex: ReturnException) {
        logger.info("${ex::class.simpleName} ${ex.message ?: ""}")
        ex.toResponseEnvelope(requestEnvelope.callId)
    } catch (ex: Exception) {
        logger.error(ex)
        ex.toResponseEnvelope(requestEnvelope.callId)
    }

    serviceAccessLog.info("${requestEnvelope.serviceName} ${requestEnvelope.funName} ${requestEnvelope.payload.size} ${responseEnvelope.status} ${responseEnvelope.payload.size} ${context.getPrincipalOrNull()}")

    send(Frame.Binary(true, encode(responseEnvelope, ResponseEnvelope)))


}
