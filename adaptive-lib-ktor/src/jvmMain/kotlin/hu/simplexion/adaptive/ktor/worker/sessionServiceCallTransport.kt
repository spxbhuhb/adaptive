/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.encode
import hu.simplexion.adaptive.auth.context.getPrincipalOrNull
import hu.simplexion.adaptive.auth.model.Session.Companion.LOGOUT_TOKEN
import hu.simplexion.adaptive.auth.model.Session.Companion.SESSION_TOKEN
import hu.simplexion.adaptive.ktor.ServerWebSocketServiceCallTransport
import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.log.AdaptiveLogger
import hu.simplexion.adaptive.log.getLogger
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.model.*
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.decode
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

val serviceAccessLog = LoggerFactory.getLogger("hu.simplexion.adaptive.service.ServiceAccessLog") !!

// FIXME flood detection, session id brute force attack detection
fun Routing.sessionWebsocketServiceCallTransport(
    useTextFrame: Boolean,
    wireFormatProvider: WireFormatProvider,
    sessionWorker: SessionWorker,
    path: String = "/adaptive/service"
) {
    webSocket(path) {

        val logger = getLogger("hu.simplexion.adaptive.ktor.worker.sessionWebsocketServiceCallTransport")
        logger.info("connection opened from ${call.request.local.remoteAddress}:${call.request.local.remotePort}")

        try {
            val sessionUuid = call.request.cookies[sessionWorker.clientIdCookieName]?.let { UUID<ServiceContext>(it) } ?: UUID()

            val context = ServiceContext(sessionUuid, wireFormatProvider = defaultWireFormatProvider)

            sessionWorker.getSessionForContext(sessionUuid)?.let {
                context.data[SESSION_TOKEN] = it
            }

            val transport = ServerWebSocketServiceCallTransport(
                useTextFrame,
                wireFormatProvider,
                this
            )

            incoming()
            for (frame in incoming) {
                val data = when (frame) {
                    is Frame.Binary -> frame.data
                    is Frame.Text -> frame.data
                    else -> continue
                }

                // if this throws an exception there is an error in the service framework
                // better to close the connection then
                val requestEnvelope = decode(data, RequestEnvelope)

                launch { serve(logger, sessionWorker, context, requestEnvelope) }
            }
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

        val service = sessionWorker.adapter?.serviceCache?.get(requestEnvelope.serviceName)?.newInstance(context)
        checkNotNull(service) { "service not found: ${requestEnvelope.serviceName}" }

        val responsePayload = transaction {
            runBlocking {
                service.dispatch(
                    requestEnvelope.funName,
                    defaultWireFormatProvider.decoder(requestEnvelope.payload)
                )
            }
        }

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

    if (context.data[LOGOUT_TOKEN] == true) {
        close(CloseReason(CloseReason.Codes.GOING_AWAY, "logout"))
    }
}

fun Exception.toResponseEnvelope(callId: UUID<RequestEnvelope>): ResponseEnvelope {
    val innerPayload = if (this is AdatClass<*>) this.encode() else byteArrayOf()
    val exceptionData = ServiceExceptionData(this::class.qualifiedName ?: this::class.simpleName ?: "<unknown>", this.message, innerPayload)

    return ResponseEnvelope(
        callId,
        ServiceCallStatus.Exception,
        encode(exceptionData, ServiceExceptionData)
    )
}