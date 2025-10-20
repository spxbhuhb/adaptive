package `fun`.adaptive.service.auth

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.service.model.ReturnException
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder

/**
 * Thrown when an access check fails for an authenticated/unauthenticated session.
 */
@Adat
class AccessDenied(
    override val message : String? = null
) : ReturnException() {
    companion object : WireFormat<AccessDenied> {

        override val wireFormatName : String
            get() = "fun.adaptive.service.auth.AccessDenied"

        override fun wireFormatEncode(encoder : WireFormatEncoder, value : AccessDenied) : WireFormatEncoder =
            encoder
                .stringOrNull(1, "message", value.message)

        override fun <ST> wireFormatDecode(source : ST, decoder : WireFormatDecoder<ST>?) : AccessDenied {
            requireNotNull(decoder)
            return AccessDenied(decoder.stringOrNull(1, "message"))
        }
    }
}