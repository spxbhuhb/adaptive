package hu.simplexion.z2.wireformat

import hu.simplexion.z2.util.UUID
import kotlin.enums.EnumEntries

/**
 * Interface for building serialized messages. Protobuf needs field number
 * JSON needs field name, hence passing both.
 */
interface MessageBuilder {

    fun pack(): ByteArray

    fun startInstance() : MessageBuilder

    fun endInstance() : MessageBuilder

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
    // Float
    // ----------------------------------------------------------------------------

    fun float(fieldNumber: Int, fieldName: String, value: Float): MessageBuilder

    fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): MessageBuilder

    fun floatList(fieldNumber: Int, fieldName: String, values: List<Float>): MessageBuilder

    fun floatListOrNull(fieldNumber: Int, fieldName: String, values: List<Float>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Double
    // ----------------------------------------------------------------------------

    fun double(fieldNumber: Int, fieldName: String, value: Double): MessageBuilder

    fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): MessageBuilder

    fun doubleList(fieldNumber: Int, fieldName: String, values: List<Double>): MessageBuilder

    fun doubleListOrNull(fieldNumber: Int, fieldName: String, values: List<Double>?): MessageBuilder

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

    fun <T> instance(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, value: T): MessageBuilder

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, value: T?): MessageBuilder

    fun <T> instanceList(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, values: List<T>): MessageBuilder

    fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, values: List<T>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E): MessageBuilder

    fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E?): MessageBuilder

    fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>): MessageBuilder

    fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>?): MessageBuilder

}