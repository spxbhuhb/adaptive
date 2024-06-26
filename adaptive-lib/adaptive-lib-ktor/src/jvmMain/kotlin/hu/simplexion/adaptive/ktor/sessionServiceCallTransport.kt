package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.log.getLogger
import hu.simplexion.adaptive.service.BasicServiceContext
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.service.model.RequestEnvelope
import hu.simplexion.adaptive.service.model.ResponseEnvelope
import hu.simplexion.adaptive.service.model.ServiceCallStatus
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.decode
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

// FIXME flood detection, session id brute force attack detection
fun Routing.sessionWebsocketServiceCallTransport(
    worker : KtorWorker,
    path: String = "/adaptive/service",
    newContext: (uuid: UUID<ServiceContext>) -> ServiceContext = { BasicServiceContext(it) }
) {
    webSocket(path) {

        val logger = getLogger("websocket")
        logger.info("connection opened from ${call.request.local.remoteAddress}:${call.request.local.remotePort}")

        class SessionClose : RuntimeException()

        suspend fun close(status: ServiceCallStatus) : Nothing {
            send(Frame.Binary(true, encode(ResponseEnvelope(UUID(), status, byteArrayOf()), ResponseEnvelope)))
            throw SessionClose()
        }

        try {
            val sessionUuid = call.request.cookies["ADAPTIVE_SESSION"]?.let { UUID<ServiceContext>(it) } ?: UUID()

            val context = newContext(sessionUuid)

//            sessionImpl.getSessionForContext(sessionUuid)?.let {
//                context.data[Session.SESSION_TOKEN_UUID] = it
//            }

            for (frame in incoming) {
                val data = when (frame) {
                    is Frame.Binary -> frame.data
                    is Frame.Text -> frame.data
                    else -> continue
                }

                // if this throws an exception there is an error in the service framework
                // better to close the connection then
                val requestEnvelope = decode(data, RequestEnvelope)

                val responseEnvelope = try {

                    val service = worker.adapter?.serviceCache?.get(requestEnvelope.serviceName)?.newInstance(context)

                    if (service != null) {

                        val responsePayload = service.dispatch(
                            requestEnvelope.funName,
                            defaultWireFormatProvider.decoder(requestEnvelope.payload)
                        )

                        ResponseEnvelope(
                            requestEnvelope.callId,
                            ServiceCallStatus.Ok,
                            responsePayload
                        )

                    } else {

                        ResponseEnvelope(
                            requestEnvelope.callId,
                            ServiceCallStatus.ServiceNotFound,
                            byteArrayOf()
                        )

                    }

                } catch (ex: Exception) {
                    logger.error(ex)
                    when (ex) {

//                        is AccessDenied -> {
//                            ResponseEnvelope(
//                                requestEnvelope.callId,
//                                ServiceCallStatus.AccessDenied,
//                                byteArrayOf()
//                            )
//                        }
//
//                        is AuthenticationFail -> {
//                            ResponseEnvelope(
//                                requestEnvelope.callId,
//                                if (ex.locked) ServiceCallStatus.AuthenticationFailLocked else ServiceCallStatus.AuthenticationFail,
//                                byteArrayOf()
//                            )
//                        }

                        else -> {
                            ResponseEnvelope(
                                requestEnvelope.callId,
                                ServiceCallStatus.Exception,
                                byteArrayOf() // FIXME
                            ).also {
//                                errorLog.warn(
//                                    "${requestEnvelope.serviceName} ${requestEnvelope.funName} ${requestEnvelope.payload.size} ${it.status} ${it.payload.size} ${context.principalOrNull}",
//                                    ex
//                                )
                            }
                        }
                    }
                }

                //accessLog(context, requestEnvelope, responseEnvelope)

                send(Frame.Binary(true, encode(responseEnvelope, ResponseEnvelope)))
//
//                if (context.data[LOGOUT_TOKEN_UUID] == true) {
//                    close(ServiceCallStatus.Logout)
//                }
            }
        } catch (ex : SessionClose) {
            // intentionally left empty, nothing to do when we simply close the session
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}