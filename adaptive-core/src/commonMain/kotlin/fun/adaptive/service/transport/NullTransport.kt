package `fun`.adaptive.service.transport

import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object NullTransport : ServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    "NullTransport"
) {

    override val wireFormatProvider: WireFormatProvider
        get() = unsupported()

    override suspend fun send(envelope: TransportEnvelope) {
        unsupported()
    }

    override fun context(): ServiceContext {
        unsupported()
    }

    override suspend fun disconnect() {
        unsupported()
    }

}