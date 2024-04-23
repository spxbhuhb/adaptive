package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
import kotlin.enums.EnumEntries

/**
 * Interface for building serialized messages. Protobuf needs field number
 * JSON needs field name, hence passing both.
 */
interface WireFormatEncoder {

    fun pack(): ByteArray

    // ----------------------------------------------------------------------------
    // Any
    // ----------------------------------------------------------------------------

    fun any(fieldNumber: Int, fieldName: String, value: Any): WireFormatEncoder

    fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): WireFormatEncoder

    fun anyList(fieldNumber: Int, fieldName: String, values: List<Any>): WireFormatEncoder

    fun anyListOrNull(fieldNumber: Int, fieldName: String, values: List<Any>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Unit
    // ----------------------------------------------------------------------------

    fun unit(fieldNumber: Int, fieldName: String, value: Unit): WireFormatEncoder

    fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): WireFormatEncoder

    fun unitList(fieldNumber: Int, fieldName: String, values: List<Unit>): WireFormatEncoder

    fun unitListOrNull(fieldNumber: Int, fieldName: String, values: List<Unit>?): WireFormatEncoder
    
    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): WireFormatEncoder

    fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): WireFormatEncoder

    fun booleanList(fieldNumber: Int, fieldName: String, values: List<Boolean>): WireFormatEncoder

    fun booleanListOrNull(fieldNumber: Int, fieldName: String, values: List<Boolean>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    fun int(fieldNumber: Int, fieldName: String, value: Int): WireFormatEncoder

    fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): WireFormatEncoder

    fun intList(fieldNumber: Int, fieldName: String, values: List<Int>): WireFormatEncoder

    fun intListOrNull(fieldNumber: Int, fieldName: String, values: List<Int>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Short
    // ----------------------------------------------------------------------------

    fun short(fieldNumber: Int, fieldName: String, value: Short): WireFormatEncoder

    fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): WireFormatEncoder

    fun shortList(fieldNumber: Int, fieldName: String, values: List<Short>): WireFormatEncoder

    fun shortListOrNull(fieldNumber: Int, fieldName: String, values: List<Short>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Byte
    // ----------------------------------------------------------------------------

    fun byte(fieldNumber: Int, fieldName: String, value: Byte): WireFormatEncoder

    fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): WireFormatEncoder

    fun byteList(fieldNumber: Int, fieldName: String, values: List<Byte>): WireFormatEncoder

    fun byteListOrNull(fieldNumber: Int, fieldName: String, values: List<Byte>?): WireFormatEncoder
    
    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    fun long(fieldNumber: Int, fieldName: String, value: Long): WireFormatEncoder

    fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): WireFormatEncoder

    fun longList(fieldNumber: Int, fieldName: String, values: List<Long>): WireFormatEncoder

    fun longListOrNull(fieldNumber: Int, fieldName: String, values: List<Long>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Float
    // ----------------------------------------------------------------------------

    fun float(fieldNumber: Int, fieldName: String, value: Float): WireFormatEncoder

    fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): WireFormatEncoder

    fun floatList(fieldNumber: Int, fieldName: String, values: List<Float>): WireFormatEncoder

    fun floatListOrNull(fieldNumber: Int, fieldName: String, values: List<Float>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Double
    // ----------------------------------------------------------------------------

    fun double(fieldNumber: Int, fieldName: String, value: Double): WireFormatEncoder

    fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): WireFormatEncoder

    fun doubleList(fieldNumber: Int, fieldName: String, values: List<Double>): WireFormatEncoder

    fun doubleListOrNull(fieldNumber: Int, fieldName: String, values: List<Double>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Char
    // ----------------------------------------------------------------------------

    fun char(fieldNumber: Int, fieldName: String, value: Char): WireFormatEncoder

    fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): WireFormatEncoder

    fun charList(fieldNumber: Int, fieldName: String, values: List<Char>): WireFormatEncoder

    fun charListOrNull(fieldNumber: Int, fieldName: String, values: List<Char>?): WireFormatEncoder
    
    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    fun string(fieldNumber: Int, fieldName: String, value: String): WireFormatEncoder

    fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): WireFormatEncoder

    fun stringList(fieldNumber: Int, fieldName: String, values: List<String>): WireFormatEncoder

    fun stringListOrNull(fieldNumber: Int, fieldName: String, values: List<String>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): WireFormatEncoder

    fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): WireFormatEncoder

    fun byteArrayList(fieldNumber: Int, fieldName: String, values: List<ByteArray>): WireFormatEncoder

    fun byteArrayListOrNull(fieldNumber: Int, fieldName: String, values: List<ByteArray>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): WireFormatEncoder

    fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): WireFormatEncoder

    fun uuidList(fieldNumber: Int, fieldName: String, values: List<UUID<*>>): WireFormatEncoder

    fun uuidListOrNull(fieldNumber: Int, fieldName: String, values: List<UUID<*>>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, fieldName: String, value: T, encoder: WireFormat<T>): WireFormatEncoder

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, value: T?, encoder: WireFormat<T>): WireFormatEncoder

    fun <T> instanceList(fieldNumber: Int, fieldName: String, values: List<T>, encoder: WireFormat<T>): WireFormatEncoder

    fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, values: List<T>?, encoder: WireFormat<T>): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Enum
    // ----------------------------------------------------------------------------

    fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, value: E, entries: EnumEntries<E>): WireFormatEncoder

    fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, value: E?, entries: EnumEntries<E>): WireFormatEncoder

    fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, values: List<E>, entries: EnumEntries<E>): WireFormatEncoder

    fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, values: List<E>?, entries: EnumEntries<E>): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // UInt
    // ----------------------------------------------------------------------------

    fun uInt(fieldNumber: Int, fieldName: String, value: UInt): WireFormatEncoder

    fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): WireFormatEncoder

    fun uIntList(fieldNumber: Int, fieldName: String, values: List<UInt>): WireFormatEncoder

    fun uIntListOrNull(fieldNumber: Int, fieldName: String, values: List<UInt>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // UShort
    // ----------------------------------------------------------------------------

    fun uShort(fieldNumber: Int, fieldName: String, value: UShort): WireFormatEncoder

    fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): WireFormatEncoder

    fun uShortList(fieldNumber: Int, fieldName: String, values: List<UShort>): WireFormatEncoder

    fun uShortListOrNull(fieldNumber: Int, fieldName: String, values: List<UShort>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // UByte
    // ----------------------------------------------------------------------------

    fun uByte(fieldNumber: Int, fieldName: String, value: UByte): WireFormatEncoder

    fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): WireFormatEncoder

    fun uByteList(fieldNumber: Int, fieldName: String, values: List<UByte>): WireFormatEncoder

    fun uByteListOrNull(fieldNumber: Int, fieldName: String, values: List<UByte>?): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // ULong
    // ----------------------------------------------------------------------------

    fun uLong(fieldNumber: Int, fieldName: String, value: ULong): WireFormatEncoder

    fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): WireFormatEncoder

    fun uLongList(fieldNumber: Int, fieldName: String, values: List<ULong>): WireFormatEncoder

    fun uLongListOrNull(fieldNumber: Int, fieldName: String, values: List<ULong>?): WireFormatEncoder
    
}