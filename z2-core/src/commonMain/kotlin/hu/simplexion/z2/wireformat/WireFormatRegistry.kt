package hu.simplexion.z2.wireformat

class WireFormatRegistry {

    val formats = mutableMapOf<String, WireFormat<*>>()

    fun <ST> decodeInstance(source: ST, wireFormatDecoder: WireFormatDecoder<ST>, type: String): Any? =
        checkNotNull(formats[type]) { "missing wire format for $type" }.wireFormatDecode(source, wireFormatDecoder)

}