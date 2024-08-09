package hu.simplexion.adaptive.process

import hu.simplexion.adaptive.service.model.TransportEnvelope
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.concurrent.Executors

abstract class PipeServiceCallTransport(
    scope: CoroutineScope,

    override val wireFormatProvider: WireFormatProvider
) : ServiceCallTransport(scope) {

    var counter = 0

    val readerScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    val writerScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())

    override suspend fun send(envelope: TransportEnvelope) {

        if (wireFormatProvider.useTextFrame) {
            io.ktor.websocket.WebSocketSession.send(io.ktor.websocket.Frame.Text(true, wireFormatProvider.encode(envelope, TransportEnvelope)))
        } else {
            io.ktor.websocket.WebSocketSession.send(io.ktor.websocket.Frame.Binary(true, wireFormatProvider.encode(envelope, TransportEnvelope)))
        }
    }

    suspend fun incoming() {


        for (frame in io.ktor.websocket.WebSocketSession.incoming) {

            val envelope =
                when (frame) {
                    is io.ktor.websocket.Frame.Binary -> io.ktor.websocket.Frame.data
                    is io.ktor.websocket.Frame.Text -> io.ktor.websocket.Frame.data
                    is io.ktor.websocket.Frame.Close -> {
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
            ReceiveChannel.cancel()
            socket?.close(io.ktor.websocket.CloseReason(io.ktor.websocket.CloseReason.Codes.GOING_AWAY, ""))
            socket = null
        }
    }

    override suspend fun stop() {
        disconnect()
    }

}