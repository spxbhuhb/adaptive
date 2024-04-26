package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder

object RawIntWireFormat : WireFormat<Int> {

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Int): WireFormatEncoder {
        encoder.rawInt(value)
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Int =
        decoder !!.rawInt(source)

}