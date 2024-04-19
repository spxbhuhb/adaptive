package hu.simplexion.z2.serialization

import hu.simplexion.z2.serialization.protobuf.ProtoSerialization

abstract class SerializationConfig {

    abstract fun messageBuilder(): MessageBuilder
    abstract fun standaloneValue(): StandaloneValue<Message>
    abstract fun toMessage(payload: ByteArray): Message

    companion object {
        val defaultSerialization = ProtoSerialization()
    }
}
