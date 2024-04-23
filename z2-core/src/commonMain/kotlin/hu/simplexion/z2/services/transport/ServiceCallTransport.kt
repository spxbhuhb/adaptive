package hu.simplexion.z2.services.transport

import hu.simplexion.z2.wireformat.WireFormatDecoder

interface ServiceCallTransport {

    suspend fun call(serviceName: String, funName: String, payload: ByteArray): WireFormatDecoder

}