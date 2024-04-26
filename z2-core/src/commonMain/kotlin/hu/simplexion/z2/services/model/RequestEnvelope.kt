package hu.simplexion.z2.services.model

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatEncoder

class RequestEnvelope(
    val callId: UUID<RequestEnvelope>,
    val serviceName: String,
    val funName: String,
    val payload: ByteArray
) : WireFormat<RequestEnvelope> {

    override val wireFormatCompanion: WireFormat<RequestEnvelope>
        get() = RequestEnvelope

    companion object : WireFormat<RequestEnvelope> {

        override val fqName: String
            get() = "hu.simplexion.z2.services.model.RequestEnvelope"


        override fun wireFormatEncode(encoder: WireFormatEncoder, value: RequestEnvelope): WireFormatEncoder =
            encoder
                .uuid(1, "callId", value.callId)
                .string(2, "serviceName", value.serviceName)
                .string(3, "funName", value.funName)
                .byteArray(4, "payload", value.payload)

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): RequestEnvelope {
            requireNotNull(decoder)
            return RequestEnvelope(
                decoder.uuid(1, "callId"),
                decoder.string(2, "serviceName"),
                decoder.string(3, "funName"),
                decoder.byteArray(4, "payload")
            )
        }
    }


}