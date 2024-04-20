package hu.simplexion.z2.services.transport

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.WireFormat

class RequestEnvelope(
    val callId: UUID<RequestEnvelope>,
    val serviceName: String,
    val funName: String,
    val payload: ByteArray
) {

    companion object : WireFormat<RequestEnvelope> {

        override fun decodeInstance(message: Message?): RequestEnvelope {
            requireNotNull(message)
            return RequestEnvelope(
                message.uuid(1, "callId"),
                message.string(2, "serviceName"),
                message.string(3, "funName"),
                message.byteArray(4, "payload")
            )
        }

        override fun encodeInstance(builder: MessageBuilder, value: RequestEnvelope): MessageBuilder =
            builder
                .startInstance()
                .uuid(1, "callId", value.callId)
                .string(2, "serviceName", value.serviceName)
                .string(3, "funName", value.funName)
                .byteArray(4, "payload", value.payload)
                .endInstance()
    }


}