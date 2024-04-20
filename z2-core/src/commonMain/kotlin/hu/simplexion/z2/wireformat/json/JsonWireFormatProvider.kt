package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormatProvider

class JsonWireFormatProvider : WireFormatProvider() {

    override fun messageBuilder(): MessageBuilder {
        return JsonMessageBuilder()
    }

    override fun standalone(): Standalone<Message> {
        @Suppress("UNCHECKED_CAST")
        return JsonStandalone as Standalone<Message>
    }

    override fun toMessage(payload: ByteArray): Message {
        return JsonMessage(payload)
    }

}