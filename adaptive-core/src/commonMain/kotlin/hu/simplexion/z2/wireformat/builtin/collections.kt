package hu.simplexion.z2.wireformat.builtin

import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder
import hu.simplexion.z2.wireformat.WireFormatKind

class ListWireFormat<T>(
    val itemWireFormat: WireFormat<T>
) : WireFormat<List<T>> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: List<T>): WireFormatEncoder {
        encoder.items(value, itemWireFormat)
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): List<T> {
        if (decoder == null) return emptyList()
        return decoder.items(source, itemWireFormat)
    }

}