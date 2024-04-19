package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.MessageBuilder
import hu.simplexion.z2.serialization.SerializationConfig
import hu.simplexion.z2.serialization.StandaloneValue

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