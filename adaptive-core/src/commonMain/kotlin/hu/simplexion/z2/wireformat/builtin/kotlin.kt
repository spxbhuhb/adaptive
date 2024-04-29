package hu.simplexion.z2.wireformat.builtin

import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder
import hu.simplexion.z2.wireformat.WireFormatKind

object BooleanWireFormat : WireFormat<Boolean> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Primitive

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Boolean): WireFormatEncoder {
        encoder.rawBoolean(value)
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Boolean =
        decoder !!.rawBoolean(source)

}

object IntWireFormat : WireFormat<Int> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Primitive

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Int): WireFormatEncoder {
        encoder.rawInt(value)
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Int =
        decoder !!.rawInt(source)

}

object StringWireFormat : WireFormat<String> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Primitive

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: String): WireFormatEncoder {
        encoder.rawString(value)
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): String =
        decoder !!.rawString(source)

}