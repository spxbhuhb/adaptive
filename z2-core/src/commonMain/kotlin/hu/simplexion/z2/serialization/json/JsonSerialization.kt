package hu.simplexion.z2.serialization.json

import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.MessageBuilder
import hu.simplexion.z2.serialization.SerializationConfig
import hu.simplexion.z2.serialization.StandaloneValue

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