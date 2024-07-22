/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.encode
import hu.simplexion.adaptive.auth.model.Session.Companion.LOGOUT_TOKEN
import hu.simplexion.adaptive.auth.model.Session.Companion.SESSION_TOKEN
import hu.simplexion.adaptive.lib.auth.context.getPrincipalOrNull
import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.log.AdaptiveLogger
import hu.simplexion.adaptive.log.ReturnException
import hu.simplexion.adaptive.log.getLogger
import hu.simplexion.adaptive.service.BasicServiceContext
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.model.RequestEnvelope
import hu.simplexion.adaptive.service.model.ResponseEnvelope
import hu.simplexion.adaptive.service.model.ServiceCallStatus
import hu.simplexion.adaptive.service.model.ServiceExceptionData
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.decode
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

val serviceAccessLog = LoggerFactory.getLogger("hu.simplexion.adaptive.service.ServiceAccessLog") !!

// FIXME flood detection, session id brute force attack detection
fun Routing.sessionWebsocketServiceCallTransport(
    sessionWorker: SessionWorker,
    path: String = "/adaptive/service",
    newContext: (uuid: UUID<ServiceContext>) -> ServiceContext = { BasicServiceContext(it) }
) {
    webSocket(path) {

        val logger = getLogger("websocket")
        logger.info("connection opened from ${call.request.local.remoteAddress}:${call.request.local.remotePort}")

        try {
            val sessionUuid = call.request.cookies[sessionWorker.sessionCookieName]?.let { UUID<ServiceContext>(it) } ?: UUID()

            val context = newContext(sessionUuid)

            sessionWorker.getSessionForContext(sessionUuid)?.let {
                context.data[SESSION_TOKEN] = it
            }

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

        val responsePayload = service.dispatch(
            requestEnvelope.funName,
            defaultWireFormatProvider.decoder(requestEnvelope.payload)
        )

        ResponseEnvelope(
            requestEnvelope.callId,
            ServiceCallStatus.Ok,
            responsePayload
        )

    } catch (ex: Exception) {
        if (ex is ReturnException) {
            logger.info("${ex::class.simpleName} ${ex.message ?: ""}")
        } else {
            logger.error(ex)
        }

        val innerPayload = if (ex is AdatClass<*>) ex.encode() else byteArrayOf()
        val exceptionData = ServiceExceptionData(ex::class.qualifiedName ?: ex::class.simpleName ?: "<unknown>", ex.message, innerPayload)

        ResponseEnvelope(
            requestEnvelope.callId,
            ServiceCallStatus.Exception,
            encode(exceptionData, ServiceExceptionData)
        )
    }

    serviceAccessLog.info("${requestEnvelope.serviceName} ${requestEnvelope.funName} ${requestEnvelope.payload.size} ${responseEnvelope.status} ${responseEnvelope.payload.size} ${context.getPrincipalOrNull()}")

    send(Frame.Binary(true, encode(responseEnvelope, ResponseEnvelope)))

    if (context.data[LOGOUT_TOKEN] == true) {
        close(CloseReason(CloseReason.Codes.GOING_AWAY, "logout"))
    }
}