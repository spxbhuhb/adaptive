package `fun`.adaptive.adat.wireformat


class AdatWireFormatError(
    val formats: List<AdatPropertyWireFormat<*>>,
    cause: Throwable? = null
) : RuntimeException(toMessage(formats, cause), cause) {

    companion object {
        fun toMessage(formats: List<AdatPropertyWireFormat<*>>, cause: Throwable?) =
            formats.joinToString(
                separator = "\n",
                prefix = "Property formats:\n"
            ) {
                "    Metadata: ${it.metadata}\n    WireFormat: ${it.wireFormat.wireFormatName}\n--------\n"
            } +
                "\n    Cause: " + cause?.stackTraceToString() + "\n"
    }
}