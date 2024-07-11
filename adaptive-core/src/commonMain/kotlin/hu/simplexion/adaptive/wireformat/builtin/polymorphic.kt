package hu.simplexion.adaptive.wireformat.builtin

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatKind

class PolymorphicWireFormat<A> : WireFormat<A> {

    override val wireFormatName: String
        get() = "*"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Primitive

    @Suppress("UNCHECKED_CAST")
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: A): WireFormatEncoder =
        encoder.rawPolymorphic(value, (value as AdatClass<*>).adatCompanion.adatWireFormat as WireFormat<A>)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): A =
        decoder !!.rawPolymorphic(source)

    @Suppress("UNCHECKED_CAST")
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: A?) =
        encoder.polymorphicOrNull(fieldNumber, fieldName, value, (value as AdatClass<*>).adatCompanion.adatWireFormat as WireFormat<A>)

    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) =
        decoder.polymorphicOrNull<A>(fieldNumber, fieldName)

}