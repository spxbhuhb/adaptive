package hu.simplexion.z2.services.transport

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.WireFormat

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
) {

    companion object : WireFormat<ResponseEnvelope> {

        override fun decodeInstance(message: Message?): ResponseEnvelope {
            requireNotNull(message)
            return ResponseEnvelope(
                message.uuid(1, "callId"),
                requireNotNull(message.int(2, "status")).let { mv -> ServiceCallStatus.entries.first { it.value == mv } },
                message.byteArray(3, "payload")
            )
        }

        override fun encodeInstance(builder: MessageBuilder, value: ResponseEnvelope): MessageBuilder =
            builder
                .startInstance()
                .uuid(1, "callId", value.callId)
                .int(2, "status", value.status.value)
                .byteArray(3, "payload", value.payload)
                .endInstance()

    }

}