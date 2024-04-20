package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.SerializationConfig
import hu.simplexion.z2.wireformat.StandaloneValue

class JsonSerialization : SerializationConfig() {

    override fun messageBuilder(): MessageBuilder {
        return JsonMessageBuilder()
    }

    override fun standaloneValue(): StandaloneValue<Message> {
        @Suppress("UNCHECKED_CAST")
        return JsonStandaloneValue as StandaloneValue<Message>
    }

    override fun toMessage(payload: ByteArray): Message {
        return JsonMessage(payload)
    }

}