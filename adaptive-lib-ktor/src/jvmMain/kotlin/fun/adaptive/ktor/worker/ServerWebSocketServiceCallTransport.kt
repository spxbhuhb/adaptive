package `fun`.adaptive.ktor.worker

import `fun`.adaptive.ktor.WebSocketServiceCallTransport
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ServerWebSocketServiceCallTransport(
    wireFormatProvider: WireFormatProvider,
    override var socket: WebSocketSession?,
    val clientId: UUID<ServiceContext>,
    val session: ServiceSession?,
    val fileTransport: KtorFileTransport,
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    wireFormatProvider
) {

    override fun context(): ServiceContext =
        ServiceContext(this, clientId, session, fileTransport)

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {

        val service = serviceImplFactory[serviceName, context]

        requireNotNull(service) { "service not found: $serviceName" }

        return withContext(Dispatchers.IO) {
            service.dispatch(funName, decoder)
        }
    }
}