package hu.simplexion.z2.services.transport

import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder
import hu.simplexion.z2.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

interface ServiceCallTransport {

    suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray

    val wireFormatEncoder: WireFormatEncoder
        get() = defaultWireFormatProvider.encoder()

    fun wireFormatDecoder(payload: ByteArray): WireFormatDecoder<*> =
        defaultWireFormatProvider.decoder(payload)

}