package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.ktor.WebSocketServiceCallTransport
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.factory.ServiceImplFactory
import hu.simplexion.adaptive.service.model.ServiceSession
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TransactionWebSocketServiceCallTransport(
    wireFormatProvider: WireFormatProvider,
    override val serviceImplFactory: ServiceImplFactory,
    override var socket: WebSocketSession?,
    val clientId: UUID<ServiceContext>,
    val session: ServiceSession?,
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    wireFormatProvider
) {

    override fun context(): ServiceContext =
        ServiceContext(clientId, session, this)

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {

        val service = serviceImplFactory[serviceName, context]

        requireNotNull(service) { "service not found: $serviceName" }

        // TODO suspend transactions in Exposed
        return newSuspendedTransaction {
            service.dispatch(funName, decoder)
        }
    }
}