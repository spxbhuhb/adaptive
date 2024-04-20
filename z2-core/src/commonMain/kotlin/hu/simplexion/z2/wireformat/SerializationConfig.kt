package hu.simplexion.z2.wireformat

import hu.simplexion.z2.wireformat.protobuf.ProtoSerialization

abstract class SerializationConfig {

    abstract fun messageBuilder(): MessageBuilder
    abstract fun standaloneValue(): StandaloneValue<Message>
    abstract fun toMessage(payload: ByteArray): Message

    companion object {
        val defaultSerialization = ProtoSerialization()
    }
}
