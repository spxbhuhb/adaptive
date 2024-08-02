package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.service.model.TransportEnvelope
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class WebSocketServiceCallTransport(
    val scope: CoroutineScope,
    val useTextFrame: Boolean,
    override val wireFormatProvider: WireFormatProvider
) : ServiceCallTransport() {

    open var socket: WebSocketSession? = null

    override suspend fun send(envelope: TransportEnvelope) {
        val safeSocket = socket ?: throw RuntimeException("service transport is not connected")

        if (useTextFrame) {
            safeSocket.send(Frame.Text(true, encode(envelope, TransportEnvelope)))
        } else {
            safeSocket.send(Frame.Binary(true, encode(envelope, TransportEnvelope)))
        }
    }

    override fun serve(envelope: TransportEnvelope) {
        val serviceName = envelope.serviceName
        val funName = envelope.funName

        if (serviceName == null || funName == null) {
            logger.warning("cannot serve call: $envelope")
            return
        }

        val context = serviceContext()

        val serviceImpl = defaultServiceImplFactory[serviceName, context]

        if (serviceImpl == null) {
            logger.warning("cannot find service $serviceName")
            return
        }

        scope.launch {
            serviceImpl.dispatch(envelope.funName !!, decoder(envelope.payload))
        }
    }

    abstract fun serviceContext(): ServiceContext

    suspend fun incoming() {
        val safeSocket = socket ?: throw RuntimeException("service transport is not connected")

        for (frame in safeSocket.incoming) {

            val envelope =
                when (frame) {
                    is Frame.Binary -> frame.data
                    is Frame.Text -> frame.data
                    else -> {
                        logger.info("unhandled frame: $frame")
                        continue
                    }
                }

            receive(envelope)
        }
    }

}