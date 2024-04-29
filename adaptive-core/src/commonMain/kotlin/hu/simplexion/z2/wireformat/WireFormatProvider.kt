package hu.simplexion.z2.wireformat

import hu.simplexion.z2.wireformat.protobuf.ProtoWireFormatProvider

abstract class WireFormatProvider {

    abstract fun encoder(): WireFormatEncoder

    //abstract fun standalone(): Standalone
    abstract fun decoder(payload: ByteArray): WireFormatDecoder<*>

    companion object {
        var defaultWireFormatProvider: WireFormatProvider = ProtoWireFormatProvider()
    }
}
