package `fun`.adaptive.ktor

import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.WireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope

abstract class WebSocketServiceCallTransport(
    scope: CoroutineScope,
    override val wireFormatProvider: WireFormatProvider
) : ServiceCallTransport(scope) {

    var counter = 0

    val socketLock = getLock()

    open var socket: WebSocketSession? = null

    override suspend fun send(envelope: TransportEnvelope) {
        val safeSocket = waitFor(responseTimeout) { socketLock.use { socket } }

        if (wireFormatProvider.useTextFrame) {
            safeSocket.send(Frame.Text(true, wireFormatProvider.encode(envelope, TransportEnvelope)))
        } else {
            safeSocket.send(Frame.Binary(true, wireFormatProvider.encode(envelope, TransportEnvelope)))
        }
    }

    suspend fun incoming() {
        val safeSocket = waitFor(responseTimeout) { socketLock.use { socket } }

        for (frame in safeSocket.incoming) {

            val envelope =
                when (frame) {
                    is Frame.Binary -> frame.data
                    is Frame.Text -> frame.data
                    is Frame.Close -> {
                        transportLog.info("close frame: $frame")
                        socketLock.use { socket = null } // to stop send sending out more frames
                        break
                    }
                    else -> {
                        transportLog.info("unhandled frame: $frame")
                        continue
                    }
                }

            receive(envelope)
        }
    }

    override suspend fun disconnect() {
        if (trace) transportLog.fine("disconnecting (counter: $counter)")

        disconnectPending()
        socketLock.use {
            socket?.incoming?.cancel()
            socket?.close(CloseReason(CloseReason.Codes.GOING_AWAY, ""))
            socket = null
        }
    }

}