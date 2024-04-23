package hu.simplexion.z2.services.model

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder

/**
 * Envelope a response payload.
 *
 * @property  [callId]   Call id from the corresponding [RequestEnvelope].
 * @property  [status]   Result status of the call.
 * @property  [payload]  Return value of the called service function.
 */
class ResponseEnvelope(
    val callId: UUID<RequestEnvelope>,
    val status: ServiceCallStatus,
    val payload: ByteArray,
) : WireFormat<ResponseEnvelope> {

    override val wireFormatCompanion: WireFormat<ResponseEnvelope>
        get() = ResponseEnvelope

    companion object : WireFormat<ResponseEnvelope> {

        override val fqName: String
            get() = "hu.simplexion.z2.services.model.ResponseEnvelope"

        override fun wireFormatDecode(decoder: WireFormatDecoder?): ResponseEnvelope {
            requireNotNull(decoder)
            return ResponseEnvelope(
                decoder.uuid(1, "callId"),
                requireNotNull(decoder.int(2, "status")).let { mv -> ServiceCallStatus.entries.first { it.value == mv } },
                decoder.byteArray(3, "payload")
            )
        }

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: ResponseEnvelope): WireFormatEncoder =
            encoder
                .uuid(1, "callId", value.callId)
                .int(2, "status", value.status.value)
                .byteArray(3, "payload", value.payload)

    }

}