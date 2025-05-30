package `fun`.adaptive.ktor.websocket

import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.waitForNotNull
import `fun`.adaptive.wireformat.WireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive

abstract class WebSocketServiceCallTransport(
    scope: CoroutineScope,
    override val wireFormatProvider: WireFormatProvider
) : ServiceCallTransport(scope) {

    var counter = 0

    val socketLock = getLock()

    open var socket: WebSocketSession? = null

    override suspend fun send(envelope: TransportEnvelope) {
        val safeSocket = waitForNotNull(responseTimeout) { socketLock.use { socket } }

        val payload = wireFormatProvider.encode(envelope, TransportEnvelope)

        val frame = if (wireFormatProvider.useTextFrame) {
            Frame.Text(true, payload)
        } else {
            Frame.Binary(true, payload)
        }

        safeSocket.send(frame)
    }

    suspend fun incoming() {
        val safeSocket = waitForNotNull(responseTimeout) { socketLock.use { socket } }

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

        val safeSocket = socketLock.use { socket.also { socket = null } } ?: return

        safeSuspendCall(transportLog) {
            if (safeSocket.isActive == true) safeSocket.close(CloseReason(CloseReason.Codes.GOING_AWAY, ""))
        }
    }

}