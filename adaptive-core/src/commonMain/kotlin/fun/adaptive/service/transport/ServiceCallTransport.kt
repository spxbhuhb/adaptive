/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service.transport

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.encode
import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.factory.ServiceImplFactory
import `fun`.adaptive.service.model.DisconnectException
import `fun`.adaptive.service.model.ReturnException
import `fun`.adaptive.service.model.ServiceExceptionData
import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

abstract class ServiceCallTransport(
    val scope: CoroutineScope
) {

    val transportLog = getLogger("fun.adaptive.service.Transport")

    val accessLog = getLogger("fun.adaptive.service.Access").also { it.level = LogLevel.Warning }

    var trace: Boolean = false

    val responseTimeout = 20.seconds

    val responseChannelLock = getLock()

    val responseChannels = mutableMapOf<UUID<TransportEnvelope>, Channel<TransportEnvelope>>()

    abstract val serviceImplFactory: ServiceImplFactory

    abstract val wireFormatProvider: WireFormatProvider

    abstract suspend fun send(envelope: TransportEnvelope)

    abstract fun context(): ServiceContext

    abstract suspend fun disconnect()

    /**
     * Handle an incoming [TransportEnvelope]. The envelope may be a call `(success == null)` or
     * a return value `(success != null)`.
     */
    fun receive(payload: ByteArray) {

        val envelope = wireFormatProvider.decode(payload, TransportEnvelope)

        if (trace) {
            transportLog.fine("receive $envelope")
            transportLog.fine("receive data: ${wireFormatProvider.dump(envelope.payload)}")
        }

        val callId = envelope.callId

        if (envelope.success == null) {
            scope.launch { serve(envelope) }
            return
        }

        val responseChannel = responseChannelLock.use {
            responseChannels.remove(callId)
        }

        if (responseChannel == null) {
            transportLog.info("dropping envelope (no response channel) $envelope")
            return
        }

        val sendResult = responseChannel.trySend(envelope)

        if (! sendResult.isSuccess) {
            transportLog.info("dropping envelope (responseChannel.trySend failed) $envelope")
        }

    }

    suspend fun serve(request: TransportEnvelope) {

        val context = context()

        val response = try {

            requireNotNull(request.serviceName)
            requireNotNull(request.funName)

            val responsePayload = dispatch(context, request.serviceName, request.funName, decoder(request.payload))

            TransportEnvelope(request.callId, null, null, true, responsePayload)

        } catch (ex: ReturnException) {

            transportLog.info("${ex::class.simpleName} ${ex.message ?: ""}")
            ex.toEnvelope(request.callId)

        } catch (ex: Exception) {

            transportLog.error(ex)
            ex.toEnvelope(request.callId)

        }

        accessLog.info("$request ${response.success} ${context.sessionOrNull?.principalOrNull}")

        try {
            send(response)
        } finally {
            if (context.disconnect) {
                disconnect()
                context.cleanup()
            }
        }
    }

    open suspend fun dispatch(
        context: ServiceContext,
        serviceName: String,
        funName: String,
        decoder: WireFormatDecoder<*>
    ): ByteArray {
        val service = serviceImplFactory[serviceName, context]

        requireNotNull(service) { "service not found: $serviceName" }

        return service.dispatch(funName, decoder)
    }

    fun Exception.toEnvelope(callId: UUID<TransportEnvelope>): TransportEnvelope {
        val exceptionData = if (this is AdatClass) {
            ServiceExceptionData(this.adatCompanion.wireFormatName, this.message, this.encode(wireFormatProvider))
        } else {
            // TODO JS does not support qualified name, how to encode exceptions properly?
            ServiceExceptionData(this::class.simpleName ?: "<unknown>", this.message, byteArrayOf())
        }

        return TransportEnvelope(callId, null, null, false, wireFormatProvider.encode(exceptionData, ServiceExceptionData))
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

            if (trace) {
                transportLog.fine("send $request")
                transportLog.fine("send data: ${wireFormatProvider.dump(request.payload)}")
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

        if (trace) transportLog.fine("${if (response.success == true) "SUCCESS" else "FAIL"} for $request")

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
        val serviceExceptionData = wireFormatProvider.decode(envelope.payload, ServiceExceptionData)

        val wireFormat = WireFormatRegistry[serviceExceptionData.className] // FIXME do we want className or wireFormatName here?

        if (wireFormat != null) {
            throw wireFormatProvider.decode(serviceExceptionData.payload, wireFormat) as Exception
        } else {
            throw ServiceCallException(serviceName, funName, envelope, serviceExceptionData)
        }
    }

    open fun disconnectPending() {
        responseChannelLock.use {

            val data = ServiceExceptionData(
                "fun.adaptive.service.model.DisconnectException",
                null,
                wireFormatProvider.encode(DisconnectException("transport is disconnected"), DisconnectException)
            )

            val payload = wireFormatProvider.encode(data, ServiceExceptionData)

            responseChannels.forEach { (callId, channel) ->
                channel.trySend(
                    TransportEnvelope(callId, null, null, false, payload)
                )
            }
        }
    }

    open suspend fun stop() {
        disconnect()
    }

    fun encoder(): WireFormatEncoder =
        wireFormatProvider.encoder()

    fun decoder(payload: ByteArray): WireFormatDecoder<*> =
        wireFormatProvider.decoder(payload)

}