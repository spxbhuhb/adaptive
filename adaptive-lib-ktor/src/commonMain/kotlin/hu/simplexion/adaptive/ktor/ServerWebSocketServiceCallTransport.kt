package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.auth.model.Session
import hu.simplexion.adaptive.auth.model.Session.Companion.SESSION_TOKEN
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.collections.mutableMapOf
import kotlin.collections.set

abstract class ServerWebSocketServiceCallTransport(
    useTextFrame: Boolean,
    wireFormatProvider: WireFormatProvider,
    override var socket: WebSocketSession?
) : WebSocketServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    useTextFrame,
    wireFormatProvider
) {

    abstract fun session(): Session?

    override fun serviceContext(): ServiceContext {
        val context = ServiceContext(UUID(), mutableMapOf(), wireFormatProvider)

        val session = session()
        if (session != null) {
            context.data[SESSION_TOKEN] = session
        }

        return context
    }

}