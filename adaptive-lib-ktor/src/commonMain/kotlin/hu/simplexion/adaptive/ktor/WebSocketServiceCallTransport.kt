package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.model.TransportEnvelope
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope

abstract class WebSocketServiceCallTransport(
    scope: CoroutineScope,
    val useTextFrame: Boolean,
    override val wireFormatProvider: WireFormatProvider
) : ServiceCallTransport(scope) {

    open var socket: WebSocketSession? = null

    override suspend fun send(envelope: TransportEnvelope) {
        val safeSocket = socket ?: throw RuntimeException("service transport is not connected")

        if (useTextFrame) {
            safeSocket.send(Frame.Text(true, encode(envelope, TransportEnvelope)))
        } else {
            safeSocket.send(Frame.Binary(true, encode(envelope, TransportEnvelope)))
        }
    }

    suspend fun incoming() {
        val safeSocket = socket ?: throw RuntimeException("service transport is not connected")

        for (frame in safeSocket.incoming) {

            val envelope =
                when (frame) {
                    is Frame.Binary -> frame.data
                    is Frame.Text -> frame.data
                    else -> {
                        transportLog.info("unhandled frame: $frame")
                        continue
                    }
                }

            receive(envelope)
        }
    }

    override suspend fun disconnect() {
        socket?.close(CloseReason(CloseReason.Codes.GOING_AWAY, ""))
    }
}