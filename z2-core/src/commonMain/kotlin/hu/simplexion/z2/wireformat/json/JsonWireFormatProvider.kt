package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder
import hu.simplexion.z2.wireformat.WireFormatProvider

class JsonWireFormatProvider : WireFormatProvider() {

    override fun encoder(): WireFormatEncoder =
        JsonWireFormatEncoder()

    override fun decoder(payload: ByteArray): WireFormatDecoder<*> =
        JsonWireFormatDecoder(payload)

}