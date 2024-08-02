package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.ktor.WebSocketServiceCallTransport
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.model.ServiceSession
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TransactionWebSocketServiceCallTransport(
    useTextFrame: Boolean,
    wireFormatProvider: WireFormatProvider,
    val adapter: AdaptiveServerAdapter,
    override var socket: WebSocketSession?,
    val session: ServiceSession?
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    useTextFrame,
    wireFormatProvider
) {

    override fun context(): ServiceContext =
        ServiceContext(UUID(), session)

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {

        val service = adapter.serviceCache[serviceName]?.newInstance(context)

        requireNotNull(service) { "service not found: $serviceName" }

        // TODO suspend transactions in Exposed
        return newSuspendedTransaction {
            service.dispatch(funName, decoder)
        }
    }
}