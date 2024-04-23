package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID

interface WireFormatDecoder {

    // -----------------------------------------------------------------------------------------
    // Any
    // -----------------------------------------------------------------------------------------

    fun any(fieldNumber: Int, fieldName: String): Any

    fun anyOrNull(fieldNumber: Int, fieldName: String): Any?

    fun anyList(fieldNumber: Int, fieldName: String): List<Any>

    fun anyListOrNull(fieldNumber: Int, fieldName: String): List<Any>?

    // -----------------------------------------------------------------------------------------
    // Unit
    // -----------------------------------------------------------------------------------------

    fun unit(fieldNumber: Int, fieldName: String): Unit

    fun unitOrNull(fieldNumber: Int, fieldName: String): Unit?

    fun unitList(fieldNumber: Int, fieldName: String): List<Unit>

    fun unitListOrNull(fieldNumber: Int, fieldName: String): List<Unit>?
    
    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, fieldName: String): Boolean

    fun booleanOrNull(fieldNumber: Int, fieldName: String): Boolean?

    fun booleanList(fieldNumber: Int, fieldName: String): List<Boolean>

    fun booleanListOrNull(fieldNumber: Int, fieldName: String): List<Boolean>?

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    fun int(fieldNumber: Int, fieldName: String): Int

    fun intOrNull(fieldNumber: Int, fieldName: String): Int?

    fun intList(fieldNumber: Int, fieldName: String): List<Int>

    fun intListOrNull(fieldNumber: Int, fieldName: String): List<Int>?

    // -----------------------------------------------------------------------------------------
    // Short
    // -----------------------------------------------------------------------------------------

    fun short(fieldNumber: Int, fieldName: String): Short

    fun shortOrNull(fieldNumber: Int, fieldName: String): Short?

    fun shortList(fieldNumber: Int, fieldName: String): List<Short>

    fun shortListOrNull(fieldNumber: Int, fieldName: String): List<Short>?

    // -----------------------------------------------------------------------------------------
    // Byte
    // -----------------------------------------------------------------------------------------

    fun byte(fieldNumber: Int, fieldName: String): Byte

    fun byteOrNull(fieldNumber: Int, fieldName: String): Byte?

    fun byteList(fieldNumber: Int, fieldName: String): List<Byte>

    fun byteListOrNull(fieldNumber: Int, fieldName: String): List<Byte>?
    
    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------

    fun long(fieldNumber: Int, fieldName: String): Long

    fun longOrNull(fieldNumber: Int, fieldName: String): Long?

    fun longList(fieldNumber: Int, fieldName: String): List<Long>

    fun longListOrNull(fieldNumber: Int, fieldName: String): List<Long>?

    // -----------------------------------------------------------------------------------------
    // Float
    // -----------------------------------------------------------------------------------------

    fun float(fieldNumber: Int, fieldName: String): Float

    fun floatOrNull(fieldNumber: Int, fieldName: String): Float?

    fun floatList(fieldNumber: Int, fieldName: String): List<Float>

    fun floatListOrNull(fieldNumber: Int, fieldName: String): List<Float>?

    // -----------------------------------------------------------------------------------------
    // Double
    // -----------------------------------------------------------------------------------------

    fun double(fieldNumber: Int, fieldName: String): Double

    fun doubleOrNull(fieldNumber: Int, fieldName: String): Double?

    fun doubleList(fieldNumber: Int, fieldName: String): List<Double>

    fun doubleListOrNull(fieldNumber: Int, fieldName: String): List<Double>?

    // -----------------------------------------------------------------------------------------
    // Char
    // -----------------------------------------------------------------------------------------

    fun char(fieldNumber: Int, fieldName: String): Char

    fun charOrNull(fieldNumber: Int, fieldName: String): Char?

    fun charList(fieldNumber: Int, fieldName: String): List<Char>

    fun charListOrNull(fieldNumber: Int, fieldName: String): List<Char>?
    
    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    fun string(fieldNumber: Int, fieldName: String): String

    fun stringOrNull(fieldNumber: Int, fieldName: String): String?

    fun stringList(fieldNumber: Int, fieldName: String): List<String>

    fun stringListOrNull(fieldNumber: Int, fieldName: String): List<String>?

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int, fieldName: String): ByteArray

    fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray?

    fun byteArrayList(fieldNumber: Int, fieldName: String): List<ByteArray>

    fun byteArrayListOrNull(fieldNumber: Int, fieldName: String): List<ByteArray>?

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T>

    fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>?

    fun <T> uuidList(fieldNumber: Int, fieldName: String): List<UUID<T>>

    fun <T> uuidListOrNull(fieldNumber: Int, fieldName: String): List<UUID<T>>?

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T?

    fun <T> instanceList(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): MutableList<T>

    fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): List<T>?

    // -----------------------------------------------------------------------------------------
    // UInt
    // -----------------------------------------------------------------------------------------

    fun uInt(fieldNumber: Int, fieldName: String): UInt

    fun uIntOrNull(fieldNumber: Int, fieldName: String): UInt?

    fun uIntList(fieldNumber: Int, fieldName: String): List<UInt>

    fun uIntListOrNull(fieldNumber: Int, fieldName: String): List<UInt>?

    // -----------------------------------------------------------------------------------------
    // UShort
    // -----------------------------------------------------------------------------------------

    fun uShort(fieldNumber: Int, fieldName: String): UShort

    fun uShortOrNull(fieldNumber: Int, fieldName: String): UShort?

    fun uShortList(fieldNumber: Int, fieldName: String): List<UShort>

    fun uShortListOrNull(fieldNumber: Int, fieldName: String): List<UShort>?

    // -----------------------------------------------------------------------------------------
    // UByte
    // -----------------------------------------------------------------------------------------

    fun uByte(fieldNumber: Int, fieldName: String): UByte

    fun uByteOrNull(fieldNumber: Int, fieldName: String): UByte?

    fun uByteList(fieldNumber: Int, fieldName: String): List<UByte>

    fun uByteListOrNull(fieldNumber: Int, fieldName: String): List<UByte>?

    // -----------------------------------------------------------------------------------------
    // ULong
    // -----------------------------------------------------------------------------------------

    fun uLong(fieldNumber: Int, fieldName: String): ULong

    fun uLongOrNull(fieldNumber: Int, fieldName: String): ULong?

    fun uLongList(fieldNumber: Int, fieldName: String): List<ULong>

    fun uLongListOrNull(fieldNumber: Int, fieldName: String): List<ULong>?
}