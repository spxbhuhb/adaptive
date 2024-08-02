package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.auth.model.Session.Companion.SESSION_TOKEN
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.collections.set

class ServerWebSocketServiceCallTransport<ST>(
    useTextFrame: Boolean,
    wireFormatProvider: WireFormatProvider,
    val adapter: AdaptiveServerAdapter,
    override var socket: WebSocketSession?,
    val session: ST?
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    useTextFrame,
    wireFormatProvider
) {

    override fun context(): ServiceContext {
        val context = ServiceContext(UUID(), null, mutableMapOf(), wireFormatProvider)

        session?.let { context.data[SESSION_TOKEN] = it }

        return context
    }

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {

        val service = adapter.serviceCache[serviceName]?.newInstance(context)

        requireNotNull(service) { "service not found: $serviceName" }

        return transaction {
            runBlocking {
                service.dispatch(
                    requestEnvelope.funName,
                    defaultWireFormatProvider.decoder(requestEnvelope.payload)
                )
            }
        }

    }
}