package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

open class ClientWebSocketServiceCallTransport(
    val servicePath: String,
    val clientIdPath: String,
    wireFormatProvider: WireFormatProvider
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
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
                counter ++
                if (trace) transportLog.fine("connecting (retryDelay=$retryDelay, counter=$counter)")

                client.webSocket(servicePath) {
                    if (trace) transportLog.fine("connected (counter=$counter)")
                    socketLock.use { socket = this }
                    incoming()
                }

            } catch (ex: CancellationException) {
                // shutdown, no error to be logged there
                currentCoroutineContext().ensureActive()
            } catch (ex: Exception) {
                if (! scope.isActive) return
                delay(retryDelay) // wait a bit before trying to re-establish the connection
                if (retryDelay < 5_000) retryDelay = (retryDelay * 115) / 100
            } finally {
                disconnect()
            }

        }
    }

    override fun context(): ServiceContext {
        return ServiceContext(UUID(), null)
    }

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {
        val serviceImpl = defaultServiceImplFactory[serviceName, context]

        requireNotNull(serviceImpl) { "cannot find service $serviceName" }

        return serviceImpl.dispatch(funName, decoder)
    }

}