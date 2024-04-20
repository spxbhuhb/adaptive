package hu.simplexion.z2.services.transport

import hu.simplexion.z2.wireformat.Message

interface ServiceCallTransport {

    suspend fun call(serviceName: String, funName: String, payload: ByteArray): Message

}