package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormatProvider

class ProtoWireFormatProvider : WireFormatProvider() {

    override fun messageBuilder(): MessageBuilder {
        return ProtoMessageBuilder()
    }

    override fun standalone(): Standalone<Message> {
        @Suppress("UNCHECKED_CAST")
        return ProtoStandalone as Standalone<Message>
    }

    override fun toMessage(payload: ByteArray): Message {
        return ProtoMessage(payload)
    }

}