package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.FqNameAware

interface WireFormat<T> : FqNameAware {

    override val fqName: String
        get() = wireFormatCompanion.fqName

    val wireFormatCompanion: WireFormat<T>
        get() {
            throw UnsupportedOperationException("This code should be replaced by the Z2 plugin for classes and never be called for companions and objects.")
        }

    fun wireFormatEncode(encoder: WireFormatEncoder, value: T): WireFormatEncoder =
        wireFormatCompanion.wireFormatEncode(encoder, value)

    fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): T =
        wireFormatCompanion.wireFormatDecode(source, decoder)

}