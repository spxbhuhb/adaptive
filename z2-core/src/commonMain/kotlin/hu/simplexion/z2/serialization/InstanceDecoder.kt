package hu.simplexion.z2.serialization

interface InstanceDecoder<T> {

    fun decodeInstance(message: Message?): T

}