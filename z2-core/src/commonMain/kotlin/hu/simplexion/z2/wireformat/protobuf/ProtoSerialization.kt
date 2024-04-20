package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.SerializationConfig
import hu.simplexion.z2.wireformat.StandaloneValue

class ProtoSerialization : SerializationConfig() {

    override fun messageBuilder(): MessageBuilder {
        return ProtoMessageBuilder()
    }

    override fun standaloneValue(): StandaloneValue<Message> {
        @Suppress("UNCHECKED_CAST")
        return ProtoStandaloneValue as StandaloneValue<Message>
    }

    override fun toMessage(payload: ByteArray): Message {
        return ProtoMessage(payload)
    }

}