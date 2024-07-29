package hu.simplexion.adaptive.service.testing

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.model.ResponseEnvelope
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

class TestServiceContext(
    val transport: TestServiceTransport
) : ServiceContext() {

    override suspend fun send(envelope: ResponseEnvelope) {
        if (transport.dump) {
            println("ResponseEnvelope(callId=${envelope.callId}, status=${envelope.status})")
            println(defaultWireFormatProvider.dump(envelope.payload))
        }
        val listener = transport[envelope.callId] ?: return
        listener.receive(envelope.callId, envelope)
    }

}