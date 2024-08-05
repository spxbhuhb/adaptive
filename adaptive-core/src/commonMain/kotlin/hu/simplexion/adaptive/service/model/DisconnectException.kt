package hu.simplexion.adaptive.service.model

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.wireformat.Wire
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

/**
 * Thrown for all pending requests when the peer closes the connection.
 */
@Wire
class DisconnectException : Exception(), AdatClass<DisconnectException> {

    companion object : WireFormat<DisconnectException> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.service.model.DisconnectException"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: DisconnectException): WireFormatEncoder =
            encoder

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): DisconnectException {
            requireNotNull(decoder)
            return DisconnectException()
        }

    }

}