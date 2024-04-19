package hu.simplexion.z2.serialization

interface InstanceEncoder<T> {

    fun encodeInstance(builder: MessageBuilder, value: T): ByteArray

}