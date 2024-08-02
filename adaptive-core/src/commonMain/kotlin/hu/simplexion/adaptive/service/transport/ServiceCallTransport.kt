/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.log.getLogger
import hu.simplexion.adaptive.service.model.ServiceExceptionData
import hu.simplexion.adaptive.service.model.TransportEnvelope
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.decode
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

abstract class ServiceCallTransport {

    val logger = getLogger("hu.simplexion.adaptive.service.transport.ServiceCallTransport")

    val trace: Boolean = false

    val responseTimeout = 20.seconds.inWholeMicroseconds

    val responseChannelLock = getLock()

    val responseChannels = mutableMapOf<UUID<TransportEnvelope>, Channel<TransportEnvelope>>()

    abstract val wireFormatProvider: WireFormatProvider

    abstract suspend fun send(envelope: TransportEnvelope)

    abstract fun serve(envelope: TransportEnvelope)

    /**
     * Handle an incoming [TransportEnvelope]. The envelope may be a call `(success == null)` or
     * a return value `(success != null)`.
     */
    fun receive(payload: ByteArray) {

        val envelope = decode(payload, TransportEnvelope)

        if (trace) {
            logger.fine("receive $envelope")
            logger.fine("receive data:\n${defaultWireFormatProvider.dump(envelope.payload)}")
        }

        val callId = envelope.callId

        if (envelope.success == null) {
            serve(envelope)
            return
        }

        val responseChannel = responseChannelLock.use {
            responseChannels.remove(callId)
        }

        if (responseChannel == null) {
            logger.info("dropping envelope (no response channel) $envelope")
            return
        }

        val sendResult = responseChannel.trySend(envelope)

        if (! sendResult.isSuccess) {
            logger.info("dropping envelope (responseChannel.trySend failed) $envelope")
        }

    }

    /**
     * Used by the generated service consumer classes to send the call. Called in the scope of
     * the service caller.
     */
    suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray {
        val callId = UUID<TransportEnvelope>()
        val request = TransportEnvelope(callId, serviceName, funName, null, payload)

        val responseChannel = Channel<TransportEnvelope>(1)

        val response = try {

            responseChannelLock.use {
                responseChannels[callId] = responseChannel
            }

            withTimeout(responseTimeout) {
                send(request)
                responseChannel.receive()
            }

        } finally {
            responseChannelLock.use {
                responseChannels.remove(callId)
            }
        }

        if (trace) logger.fine("${if (response.success == true) "SUCCESS" else "FAIL"} for $request")

        if (response.success == true) {
            return response.payload
        }

        responseError(serviceName, funName, response)
    }

    /**
     * Called in the scope of the service caller. This function MUST throw an exception, otherwise the
     * service call would be hanging forever.
     */
    open fun responseError(serviceName: String, funName: String, envelope: TransportEnvelope): Nothing {
        val serviceExceptionData = decode(envelope.payload, ServiceExceptionData)

        val wireFormat = WireFormatRegistry[serviceExceptionData.className] // FIXME do we want className or wireFormatName here?

        if (wireFormat != null) {
            throw decode(serviceExceptionData.payload, wireFormat) as Exception
        } else {
            throw ServiceCallException(serviceName, funName, envelope, serviceExceptionData)
        }
    }

    fun encoder(): WireFormatEncoder =
        wireFormatProvider.encoder()

    fun decoder(payload: ByteArray): WireFormatDecoder<*> =
        wireFormatProvider.decoder(payload)

}