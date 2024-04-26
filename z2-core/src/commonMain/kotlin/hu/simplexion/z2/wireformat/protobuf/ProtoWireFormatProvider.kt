package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder
import hu.simplexion.z2.wireformat.WireFormatProvider

class ProtoWireFormatProvider : WireFormatProvider() {

    override fun encoder(): WireFormatEncoder =
        ProtoWireFormatEncoder()

    override fun decoder(payload: ByteArray): WireFormatDecoder<*> =
        ProtoWireFormatDecoder(payload)

    override fun standalone(): Standalone =
        ProtoStandalone

}