package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
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
    // Any
    // ----------------------------------------------------------------------------

    fun any(fieldNumber: Int, fieldName: String, value: Any): MessageBuilder

    fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): MessageBuilder

    fun anyList(fieldNumber: Int, fieldName: String, values: List<Any>): MessageBuilder

    fun anyListOrNull(fieldNumber: Int, fieldName: String, values: List<Any>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Unit
    // ----------------------------------------------------------------------------

    fun unit(fieldNumber: Int, fieldName: String, value: Unit): MessageBuilder

    fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): MessageBuilder

    fun unitList(fieldNumber: Int, fieldName: String, values: List<Unit>): MessageBuilder

    fun unitListOrNull(fieldNumber: Int, fieldName: String, values: List<Unit>?): MessageBuilder
    
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
    // Short
    // ----------------------------------------------------------------------------

    fun short(fieldNumber: Int, fieldName: String, value: Short): MessageBuilder

    fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): MessageBuilder

    fun shortList(fieldNumber: Int, fieldName: String, values: List<Short>): MessageBuilder

    fun shortListOrNull(fieldNumber: Int, fieldName: String, values: List<Short>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // Byte
    // ----------------------------------------------------------------------------

    fun byte(fieldNumber: Int, fieldName: String, value: Byte): MessageBuilder

    fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): MessageBuilder

    fun byteList(fieldNumber: Int, fieldName: String, values: List<Byte>): MessageBuilder

    fun byteListOrNull(fieldNumber: Int, fieldName: String, values: List<Byte>?): MessageBuilder
    
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
    // Char
    // ----------------------------------------------------------------------------

    fun char(fieldNumber: Int, fieldName: String, value: Char): MessageBuilder

    fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): MessageBuilder

    fun charList(fieldNumber: Int, fieldName: String, values: List<Char>): MessageBuilder

    fun charListOrNull(fieldNumber: Int, fieldName: String, values: List<Char>?): MessageBuilder
    
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
    // Enum
    // ----------------------------------------------------------------------------

    fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E): MessageBuilder

    fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E?): MessageBuilder

    fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>): MessageBuilder

    fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // UInt
    // ----------------------------------------------------------------------------

    fun uInt(fieldNumber: Int, fieldName: String, value: UInt): MessageBuilder

    fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): MessageBuilder

    fun uIntList(fieldNumber: Int, fieldName: String, values: List<UInt>): MessageBuilder

    fun uIntListOrNull(fieldNumber: Int, fieldName: String, values: List<UInt>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // UShort
    // ----------------------------------------------------------------------------

    fun uShort(fieldNumber: Int, fieldName: String, value: UShort): MessageBuilder

    fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): MessageBuilder

    fun uShortList(fieldNumber: Int, fieldName: String, values: List<UShort>): MessageBuilder

    fun uShortListOrNull(fieldNumber: Int, fieldName: String, values: List<UShort>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // UByte
    // ----------------------------------------------------------------------------

    fun uByte(fieldNumber: Int, fieldName: String, value: UByte): MessageBuilder

    fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): MessageBuilder

    fun uByteList(fieldNumber: Int, fieldName: String, values: List<UByte>): MessageBuilder

    fun uByteListOrNull(fieldNumber: Int, fieldName: String, values: List<UByte>?): MessageBuilder

    // ----------------------------------------------------------------------------
    // ULong
    // ----------------------------------------------------------------------------

    fun uLong(fieldNumber: Int, fieldName: String, value: ULong): MessageBuilder

    fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): MessageBuilder

    fun uLongList(fieldNumber: Int, fieldName: String, values: List<ULong>): MessageBuilder

    fun uLongListOrNull(fieldNumber: Int, fieldName: String, values: List<ULong>?): MessageBuilder
    
}