package `fun`.adaptive.service.model

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.wireformat.Wire
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder

/**
 * Thrown for all pending requests when the peer closes the connection.
 */
@Wire
class DisconnectException : Exception(), AdatClass<DisconnectException> {

    companion object : WireFormat<DisconnectException> {

        override val wireFormatName: String
            get() = "fun.adaptive.service.model.DisconnectException"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: DisconnectException): WireFormatEncoder =
            encoder

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): DisconnectException {
            requireNotNull(decoder)
            return DisconnectException()
        }

    }

}