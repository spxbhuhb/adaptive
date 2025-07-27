package `fun`.adaptive.ktor.websocket

import `fun`.adaptive.ktor.api.toHttp
import `fun`.adaptive.ktor.api.toWs
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.transport.DelayReconnectException
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.close
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

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
 * @param  setupFun             A function to be called after the websocket is established.
 */
open class ClientWebSocketServiceCallTransport(
    host: String,
    servicePath: String,
    clientIdPath: String,
    wireFormatProvider: WireFormatProvider,
    val setupFun : suspend (transport: ServiceCallTransport) -> Unit
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    wireFormatProvider
) {

    var started : Boolean = false

    val clientIdUrl = host.toHttp(clientIdPath)
    val serviceUrl = host.toWs(servicePath)

    val baseRetryDelay = 200L
    var retryDelay = baseRetryDelay // milliseconds
    var retrying = false

    val client = HttpClient {
        install(HttpCookies)
        install(WebSockets) {
            pingInterval = 20.seconds
        }
    }

    override suspend fun start() : ServiceCallTransport {
        if (started) return this
        started = true

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
                transportLog.fine { "connecting (attempt # $counter)" }

                client.get(clientIdUrl) // initialize the client id cookie

                client.webSocket(serviceUrl) {

                    transportLog.info { "WS-CONNECT $serviceUrl (attempt # $counter)" }

                    counter = 0
                    retryDelay = baseRetryDelay // reset to base if successfully connected
                    socketLock.use { socket = this }

                    launch {
                        try {
                            setupFun(this@ClientWebSocketServiceCallTransport)
                        } catch (ex : Exception) {
                            if (ex is DelayReconnectException) {
                                retryDelay = ex.delay.inWholeMilliseconds
                                transportLog.fine { "setup requested reconnect delay of $retryDelay ms" }
                            }
                            this@webSocket.close()
                            throw ex
                        }
                    }

                    incoming()
                }

            } catch (ex: Exception) {
                // shutdown, no error to be logged there, no retry
                currentCoroutineContext().ensureActive()

                if (counter == 0) {
                    transportLog.info { "WS-DISCONNECT $serviceUrl" }
                    retrying = true
                }

                // connection problems are normal, we should not spam the error log with them
                transportLog.fine(ex)

                scope.ensureActive()

            } finally {
                disconnect()
            }

            transportLog.fine { "waiting before next attempt ($retryDelay ms)" }

            delay(retryDelay) // wait a bit before trying to re-establish the connection

            // slowly increment delay, first retry should be fast, but we should not
            // spam reconnecting very fast if the connection is truly broken
            if (retryDelay < 5_000) retryDelay = (retryDelay * 115) / 100

            // limit the max delay to 5 seconds
            if (retryDelay > 5_000) retryDelay = 5_000
        }
    }

    override fun context(): ServiceContext {
        return ServiceContext(transport = this)
    }

}