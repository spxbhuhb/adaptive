package hu.simplexion.z2.services.testing

import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.transport.ServiceCallTransport

class TestServiceTransport(
    val serviceImpl: ServiceImpl<*>,
    val dump: Boolean = false
) : ServiceCallTransport {

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray {
        if (dump) {
            println("==== REQUEST ====")
            println(serviceName)
            println(funName)
            println(payload.decodeToString())
        }

        val responsePayload = serviceImpl.dispatch(funName, wireFormatDecoder(payload))

        if (dump) {
            println("==== RESPONSE ====")
            println(responsePayload)
        }

        return responsePayload
    }
}