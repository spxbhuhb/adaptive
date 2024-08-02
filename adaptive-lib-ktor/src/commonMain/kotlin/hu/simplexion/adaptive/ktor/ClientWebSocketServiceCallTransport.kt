package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

open class ClientWebSocketServiceCallTransport(
    val servicePath: String = "/adaptive/service",
    val clientIdPath: String = "/adaptive/client-id",
    useTextFrame: Boolean,
    wireFormatProvider: WireFormatProvider
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    useTextFrame,
    wireFormatProvider
) {

    var retryDelay = 200L // milliseconds

    val client = HttpClient {
        install(HttpCookies)
        install(WebSockets) {
            pingInterval = 20_000
        }
    }

    suspend fun start() {
        client.get(clientIdPath) // initialize the client id cookie
        scope.launch { run() }
    }

    suspend fun run() {
        while (scope.isActive) {

            try {
                if (trace) transportLog.fine("connecting (retryDelay=$retryDelay)")

                client.webSocket(servicePath) {
                    if (trace) transportLog.fine("connected")
                    socket = this
                    incoming()
                }

            } catch (ex: CancellationException) {
                // the `for` is cancelled, this probably means a shutdown
                throw ex
            } catch (ex: Exception) {
                if (! scope.isActive) return
                delay(retryDelay) // wait a bit before trying to re-establish the connection
                if (retryDelay < 5_000) retryDelay = (retryDelay * 115) / 100
            }
        }
    }

    override fun context(): ServiceContext {
        return ServiceContext(UUID(), null, mutableMapOf(), wireFormatProvider)
    }

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {
        val serviceImpl = defaultServiceImplFactory[serviceName, context]

        requireNotNull(serviceImpl) { "cannot find service $serviceName" }

        return serviceImpl.dispatch(funName, decoder)
    }

}