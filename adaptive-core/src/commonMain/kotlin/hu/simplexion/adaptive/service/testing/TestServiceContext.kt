package hu.simplexion.adaptive.service.testing

import hu.simplexion.adaptive.service.ServiceContext

class TestServiceContext(
    transport: TestServiceTransport
) : ServiceContext(
    transport = transport
) {

//    override suspend fun send(envelope: TransportEnvelope) {
//        if (transport.dump) {
//            println("ResponseEnvelope(callId=${envelope.callId}, status=${envelope.success})")
//            println(defaultWireFormatProvider.dump(envelope.payload))
//        }
//        val listener = transport[envelope.callId] ?: return
//        listener.receive(envelope.callId, envelope)
//    }

}