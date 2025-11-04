package `fun`.adaptive.ktor.worker

import `fun`.adaptive.ktor.websocket.WebSocketServiceCallTransport
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.transport.ServiceSessionProvider
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServerWebSocketServiceCallTransport(
    wireFormatProvider: WireFormatProvider,
    override var socket: WebSocketSession?,
    val sessionId: UUID<ServiceContext>,
    val fileTransport: KtorFileTransport,
    val sessionProvider: ServiceSessionProvider? = null,
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    wireFormatProvider
) {

    override fun context(): ServiceContext =
        // Resolve session dynamically to keep lastActivity updated via provider (if present)
        ServiceContext(this, sessionId, sessionProvider?.getSession(sessionId), fileTransport)

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {

        val service = serviceImplFactory[serviceName, context]

        requireNotNull(service) { "service not found: $serviceName" }

        return withContext(Dispatchers.IO) {
            service.dispatch(funName, decoder)
        }
    }
}