package hu.simplexion.z2.serialization

import hu.simplexion.z2.util.UUID

/**
 * Interface for building serialized messages. Protobuf needs field number
 * JSON needs field name, hence passing both.
 */
interface MessageBuilder {

    fun pack(): ByteArray

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): MessageBuilder

    fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): MessageBuilder

    fun booleanList(fieldNumber: Int, fieldName: String, values: List<Boolean>): MessageBuilder

    fun booleanListOrNull(fieldNumber: Int, fieldName: String, values: List<Boolean>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    fun int(fieldNumber: Int, fieldName: String, value: Int): MessageBuilder

    fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): MessageBuilder

    fun intList(fieldNumber: Int, fieldName: String, values: List<Int>): MessageBuilder

    fun intListOrNull(fieldNumber: Int, fieldName: String, values: List<Int>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    fun long(fieldNumber: Int, fieldName: String, value: Long): MessageBuilder

    fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): MessageBuilder

    fun longList(fieldNumber: Int, fieldName: String, values: List<Long>): MessageBuilder

    fun longListOrNull(fieldNumber: Int, fieldName: String, values: List<Long>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    fun string(fieldNumber: Int, fieldName: String, value: String): MessageBuilder

    fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): MessageBuilder

    fun stringList(fieldNumber: Int, fieldName: String, values: List<String>): MessageBuilder

    fun stringListOrNull(fieldNumber: Int, fieldName: String, values: List<String>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): MessageBuilder

    fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): MessageBuilder

    fun byteArrayList(fieldNumber: Int, fieldName: String, values: List<ByteArray>): MessageBuilder

    fun byteArrayListOrNull(fieldNumber: Int, fieldName: String, values: List<ByteArray>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): MessageBuilder

    fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): MessageBuilder

    fun uuidList(fieldNumber: Int, fieldName: String, values: List<UUID<*>>): MessageBuilder

    fun uuidListOrNull(fieldNumber: Int, fieldName: String, values: List<UUID<*>>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, value: T): MessageBuilder

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, value: T?): MessageBuilder

    fun <T> instanceList(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, values: List<T>): MessageBuilder

    fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, values: List<T>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    /**
     * Create an empty builder for building parts of the message.
     * Intended use is `InstanceEncoder.encodeInstance`.
     */
    fun subBuilder(): MessageBuilder
}