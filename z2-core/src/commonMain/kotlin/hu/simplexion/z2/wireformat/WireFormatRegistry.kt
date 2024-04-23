package hu.simplexion.z2.wireformat

class WireFormatRegistry {

    val formats = mutableMapOf<String, WireFormat<*>>()

    fun decodeInstance(wireFormatDecoder: WireFormatDecoder, type: String): Any? =
        checkNotNull(formats[type]) { "missing wire format for $type" }.wireFormatDecode(wireFormatDecoder)

}