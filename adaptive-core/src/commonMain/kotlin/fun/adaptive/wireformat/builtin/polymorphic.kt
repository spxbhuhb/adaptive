package `fun`.adaptive.wireformat.builtin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.WireFormatKind

object PolymorphicWireFormat : WireFormat<Any> {

    override val wireFormatName: String
        get() = "*"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Primitive

    @Suppress("UNCHECKED_CAST")
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Any): WireFormatEncoder =
        encoder.rawPolymorphic(value, (value as AdatClass).adatCompanion.adatWireFormat as WireFormat<Any>)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Any =
        decoder !!.rawPolymorphic(source)

    @Suppress("UNCHECKED_CAST")
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Any?) =
        encoder.polymorphicOrNull(fieldNumber, fieldName, value, (value as AdatClass).adatCompanion.adatWireFormat as WireFormat<Any>)

    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) =
        decoder.polymorphicOrNull<Any>(fieldNumber, fieldName)

}