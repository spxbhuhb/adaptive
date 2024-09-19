package `fun`.adaptive.ktor

import `fun`.adaptive.ktor.api.toHttp
import `fun`.adaptive.ktor.api.toWs
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.factory.ServiceImplFactory
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

/**
 * Opens a websocket to [host] and [servicePath] and sends service calls over it.
 *
 * [serviceImplFactory] passed to [start] is typically the client backend that serves
 * calls received from the server.
 *
 * @param  host                 Protocol, host and port of the server, like `https://adaptive.fun`
 * @param  servicePath          The path on which the peer of the transport listens, default is `/adaptive/service-ws`.
 * @param  clientIdPath         The path which provides a client id, default is `/adaptive/client-id`.
 * @param  wireFormatProvider   The wire format to use, default is `Json`.
 */
open class ClientWebSocketServiceCallTransport(
    host: String,
    servicePath: String,
    clientIdPath: String,
    wireFormatProvider: WireFormatProvider
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    wireFormatProvider
) {

    val clientIdUrl = host.toHttp(clientIdPath)
    val serviceUrl = host.toWs(servicePath)

    var retryDelay = 200L // milliseconds

    val client = HttpClient {
        install(HttpCookies)
        install(WebSockets) {
            pingInterval = 20_000
        }
    }

    override suspend fun start(serviceImplFactory: ServiceImplFactory) : ServiceCallTransport {
        super.start(serviceImplFactory)
        client.get(clientIdUrl) // initialize the client id cookie
        scope.launch { run() }
        return this
    }

    override suspend fun stop() {
        super.stop()
        client.close()
        scope.cancel()
    }

    suspend fun run() {
        while (scope.isActive) {

            try {
                counter ++
                if (trace) transportLog.fine("connecting (retryDelay=$retryDelay, counter=$counter)")

                client.webSocket(serviceUrl) {
                    if (trace) transportLog.fine("connected (counter=$counter)")
                    socketLock.use { socket = this }
                    incoming()
                }

            } catch (ex: Exception) {
                // shutdown, no error to be logged there, no retry
                currentCoroutineContext().ensureActive()

                transportLog.error(ex)

                if (! scope.isActive) return
                delay(retryDelay) // wait a bit before trying to re-establish the connection
                if (retryDelay < 5_000) retryDelay = (retryDelay * 115) / 100
            } finally {
                disconnect()
            }

        }
    }

    override fun context(): ServiceContext {
        return ServiceContext(transport = this)
    }

}