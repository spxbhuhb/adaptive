package hu.simplexion.z2.wireformat

interface WireFormat<T> {

    fun encodeInstance(builder: MessageBuilder, value: T): MessageBuilder

    fun decodeInstance(message: Message?): T

}