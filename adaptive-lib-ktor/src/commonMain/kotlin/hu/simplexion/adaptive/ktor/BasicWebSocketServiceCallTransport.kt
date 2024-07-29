package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.log.getLogger
import hu.simplexion.adaptive.service.ServiceResponseEndpoint
import hu.simplexion.adaptive.service.model.RequestEnvelope
import hu.simplexion.adaptive.service.model.ResponseEnvelope
import hu.simplexion.adaptive.service.model.ServiceCallStatus
import hu.simplexion.adaptive.service.model.ServiceExceptionData
import hu.simplexion.adaptive.service.transport.ServiceCallException
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.service.transport.ServiceErrorHandler
import hu.simplexion.adaptive.service.transport.ServiceResponseListener
import hu.simplexion.adaptive.service.transport.ServiceTimeoutException
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.utility.vmNowMicro
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.decode
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import hu.simplexion.adaptive.wireformat.WireFormatRegistry
import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.collections.set

open class BasicWebSocketServiceCallTransport(
    val path: String = "/adaptive/service",
    var errorHandler: ServiceErrorHandler? = null,
    val trace: Boolean = false,
    val useTextFrame: Boolean = false
) : ServiceCallTransport() {

    val logger = getLogger("hu.simplexion.adaptive.ktor.BasicWebSocketServiceCallTransport")

    val callTimeout = 20_000_000

    class OutgoingCall(
        val request: RequestEnvelope,
        val createdMicros: Long = vmNowMicro(),
        val responseChannel: Channel<ResponseEnvelope> = Channel(1)
    ) {
        override fun toString(): String =
            "$createdMicros ${request.callId} ${request.serviceName} ${request.funName} ${request.payload.size}"
    }

    var retryDelay = 200L // milliseconds
    val scope = CoroutineScope(Dispatchers.Default)
    var socket: WebSocketSession? = null

    val outgoingLock = getLock()
    private val outgoingCalls = Channel<OutgoingCall>(Channel.UNLIMITED)
    val pendingCalls = mutableMapOf<UUID<RequestEnvelope>, OutgoingCall>()

    val listenerLock = getLock()
    val listeners = mutableMapOf<ServiceResponseEndpoint, ServiceResponseListener>()

    val client = HttpClient {
        install(HttpCookies)
        install(WebSockets) {
            pingInterval = 20_000
        }
    }

    fun start() {
        scope.launch { run() }
        scope.launch { timeout() }
    }

    suspend fun run() {
        var outgoing: Job? = null

        while (scope.isActive) {
            try {
                if (trace) logger.fine("connecting (retryDelay=$retryDelay)")

                client.webSocket(path) {

                    socket = this

                    if (trace) logger.fine("connected")

                    retryDelay = 200 // reset the retry delay as we have a working connection

                    outgoing = launch { outgoing() }
                    incoming()

                }

            } catch (ex: CancellationException) {
                // the `for` is cancelled, this probably means a shutdown
                throw ex
            } catch (ex: Exception) {
                outgoing?.cancelAndJoin()

                connectionError(ex)

                if (! scope.isActive) return

                delay(retryDelay) // wait a bit before trying to re-establish the connection
                if (retryDelay < 5_000) retryDelay = (retryDelay * 115) / 100
            }
        }
    }

    private suspend fun DefaultWebSocketSession.outgoing() {
        try {
            for (call in outgoingCalls) {
                if (trace) {
                    logger.fine("send $call")
                    logger.fine("send data:\n${defaultWireFormatProvider.dump(call.request.payload)}")
                }

                try {
                    if (useTextFrame) {
                        send(Frame.Text(true, encode(call.request, RequestEnvelope)))
                    } else {
                        send(Frame.Binary(true, encode(call.request, RequestEnvelope)))
                    }
                    pendingCalls[call.request.callId] = call
                } catch (_: CancellationException) {
                    postponeAfterCancel(call)
                    // break to get out of for, so we can retry
                    break
                }
            }
        } catch (ex: CancellationException) {
            // the `for` is cancelled, this probably means a shutdown
            throw ex
        } catch (ex: Exception) {
            logger.error(ex)
        }
    }

    private suspend fun DefaultWebSocketSession.incoming() {
        for (frame in incoming) {

            frame as? Frame.Binary ?: continue

            val responseEnvelope = decode(frame.data, ResponseEnvelope)

            if (trace) {
                logger.fine("receive ${responseEnvelope.callId} ${responseEnvelope.status}")
                logger.fine("send data:\n${defaultWireFormatProvider.dump(responseEnvelope.payload)}")
            }

            val callId = responseEnvelope.callId
            val call = pendingCalls.remove(callId)

            if (call != null) {
                call.responseChannel.send(responseEnvelope)
                return
            }

            val listener = listenerLock.use { listeners[callId] }
            if (listener != null) {
                launch { listener.receive(callId, responseEnvelope) }
                return
            }

            // drop the message silently as this might happen during normal operation if:
            // * the message for a listener is sent by the peer just before the listener disconnects
            // * a call went to timeout and the response arrived later

            logger.info("dropping message: $responseEnvelope")
        }
    }

    private fun postponeAfterCancel(call: OutgoingCall) {
        outgoingLock.use {

            val cutoff = vmNowMicro() - callTimeout

            // if we get a cancellation exception we try to reopen the connection and retry the calls
            // however the call that failed is removed from [outgoingCalls], so we copy remove all
            // entries from [outgoingCalls] and put them in again, here [outgoingCalls] is protected by
            // [outgoingLock], so this copy should be fine even if someone calls [call] during the copy

            val calls = mutableListOf<OutgoingCall>()

            var next: OutgoingCall? = call

            while (next != null) {
                if (next.createdMicros < cutoff) {
                    next.responseChannel.trySend(ResponseEnvelope(next.request.callId, ServiceCallStatus.Timeout, ByteArray(0)))
                } else {
                    calls += next
                }
                next = outgoingCalls.tryReceive().getOrNull()
            }

            calls.forEach { outgoingCalls.trySend(it) }
        }
    }

    /**
     * Called in the scope of [run].
     */
    open fun connectionError(ex: Exception) {
        errorHandler?.connectionError(ex)
        ex.printStackTrace()
    }

    suspend fun timeout() {
        while (scope.isActive) {
            val cutoff = vmNowMicro() - callTimeout

            pendingCalls
                .filter { it.value.createdMicros < cutoff }
                .toList()
                .forEach { (callId, call) ->
                    pendingCalls.remove(callId)
                    call.responseChannel.trySend(ResponseEnvelope(callId, ServiceCallStatus.Timeout, ByteArray(0)))
                }

            delay(1_000)
        }
    }

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray {
        OutgoingCall(RequestEnvelope(UUID(), serviceName, funName, payload)).let { outgoingCall ->

            outgoingLock.use {
                outgoingCalls.send(outgoingCall)
            }

            val responseEnvelope = outgoingCall.responseChannel.receive()
            if (trace) logger.fine("response for $serviceName.$funName ${responseEnvelope.status}")

            when (responseEnvelope.status) {
                ServiceCallStatus.Ok -> {
                    return responseEnvelope.payload
                }

                ServiceCallStatus.Logout -> {
                    try {
                        socket?.close()
                    } catch (ex: Exception) {
                        logger.warning(ex)
                    }
                    return responseEnvelope.payload
                }

                ServiceCallStatus.Timeout -> {
                    timeoutError(serviceName, funName, responseEnvelope)
                    throw RuntimeException("$serviceName  $funName  ${responseEnvelope.status}")
                }

                else -> {
                    responseError(serviceName, funName, responseEnvelope)
                    throw RuntimeException("$serviceName  $funName  ${responseEnvelope.status}")
                }
            }
        }
    }

    override fun connect(endpoint: ServiceResponseEndpoint, listener: ServiceResponseListener) {
        listenerLock.use {
            listeners[endpoint] = listener
        }
    }

    override fun disconnect(endpoint: ServiceResponseEndpoint) {
        listenerLock.use {
            listeners.remove(endpoint)
        }
    }

    /**
     * Called in the scope of the service caller. This function MUST throw an exception, otherwise the
     * service call will be hanging forever.
     */
    open fun timeoutError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {
        errorHandler?.callError(serviceName, funName, responseEnvelope, null)
        throw ServiceTimeoutException(serviceName, funName, responseEnvelope)
    }

    /**
     * Called in the scope of the service caller. This function MUST throw an exception, otherwise the
     * service call will be hanging forever.
     */
    open fun responseError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {
        val serviceExceptionData = decode(responseEnvelope.payload, ServiceExceptionData)

        errorHandler?.callError(serviceName, funName, responseEnvelope, serviceExceptionData)

        val wireFormat = WireFormatRegistry[serviceExceptionData.className] // FIXME do we want className or wireFormatName here?

        if (wireFormat != null) {
            throw decode(serviceExceptionData.payload, wireFormat) as Exception
        } else {
            throw ServiceCallException(serviceName, funName, responseEnvelope, serviceExceptionData)
        }
    }

}