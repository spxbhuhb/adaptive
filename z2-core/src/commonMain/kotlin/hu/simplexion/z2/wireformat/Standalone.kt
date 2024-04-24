package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
import kotlin.enums.EnumEntries

interface Standalone {

    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    fun encodeAny(value: Any?): ByteArray

    fun encodeAnyList(value: List<Any>?): ByteArray

    fun decodeAny(source: ByteArray): Any

    fun decodeAnyOrNull(source: ByteArray): Any?

    fun decodeAnyList(source: ByteArray): List<Any>

    fun decodeAnyListOrNull(source: ByteArray): List<Any>?

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    fun encodeUnit(value: Unit?): ByteArray

    fun encodeUnitList(value: List<Unit>?): ByteArray

    fun decodeUnit(source: ByteArray)

    fun decodeUnitOrNull(source: ByteArray): Unit?

    fun decodeUnitList(source: ByteArray): List<Unit>

    fun decodeUnitListOrNull(source: ByteArray): List<Unit>?
    
    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    fun encodeBoolean(value: Boolean?): ByteArray

    fun encodeBooleanList(value: List<Boolean>?): ByteArray

    fun decodeBoolean(source: ByteArray): Boolean

    fun decodeBooleanOrNull(source: ByteArray): Boolean?

    fun decodeBooleanList(source: ByteArray): List<Boolean>

    fun decodeBooleanListOrNull(source: ByteArray): List<Boolean>?

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    fun encodeInt(value: Int?): ByteArray

    fun encodeIntList(value: List<Int>?): ByteArray

    fun decodeInt(source: ByteArray): Int

    fun decodeIntOrNull(source: ByteArray): Int?

    fun decodeIntList(source: ByteArray): List<Int>

    fun decodeIntListOrNull(source: ByteArray): List<Int>?

    // ---------------------------------------------------------------------------
    // Short
    // ---------------------------------------------------------------------------

    fun encodeShort(value: Short?): ByteArray

    fun encodeShortList(value: List<Short>?): ByteArray

    fun decodeShort(source: ByteArray): Short

    fun decodeShortOrNull(source: ByteArray): Short?

    fun decodeShortList(source: ByteArray): List<Short>

    fun decodeShortListOrNull(source: ByteArray): List<Short>?

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    fun encodeByte(value: Byte?): ByteArray

    fun encodeByteList(value: List<Byte>?): ByteArray

    fun decodeByte(source: ByteArray): Byte

    fun decodeByteOrNull(source: ByteArray): Byte?

    fun decodeByteList(source: ByteArray): List<Byte>

    fun decodeByteListOrNull(source: ByteArray): List<Byte>?
    
    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    fun encodeLong(value: Long?): ByteArray

    fun encodeLongList(value: List<Long>?): ByteArray

    fun decodeLong(source: ByteArray): Long

    fun decodeLongOrNull(source: ByteArray): Long?

    fun decodeLongList(source: ByteArray): List<Long>

    fun decodeLongListOrNull(source: ByteArray): List<Long>?

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    fun encodeFloat(value: Float?): ByteArray

    fun encodeFloatList(value: List<Float>?): ByteArray

    fun decodeFloat(source: ByteArray): Float

    fun decodeFloatOrNull(source: ByteArray): Float?

    fun decodeFloatList(source: ByteArray): List<Float>

    fun decodeFloatListOrNull(source: ByteArray): List<Float>?

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    fun encodeDouble(value: Double?): ByteArray

    fun encodeDoubleList(value: List<Double>?): ByteArray

    fun decodeDouble(source: ByteArray): Double

    fun decodeDoubleOrNull(source: ByteArray): Double?

    fun decodeDoubleList(source: ByteArray): List<Double>

    fun decodeDoubleListOrNull(source: ByteArray): List<Double>?

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    fun encodeChar(value: Char?): ByteArray

    fun encodeCharList(value: List<Char>?): ByteArray

    fun decodeChar(source: ByteArray): Char

    fun decodeCharOrNull(source: ByteArray): Char?

    fun decodeCharList(source: ByteArray): List<Char>

    fun decodeCharListOrNull(source: ByteArray): List<Char>?
    
    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    fun encodeString(value: String?): ByteArray

    fun encodeStringList(value: List<String>?): ByteArray

    fun decodeString(source: ByteArray): String

    fun decodeStringOrNull(source: ByteArray): String?

    fun decodeStringList(source: ByteArray): List<String>

    fun decodeStringListOrNull(source: ByteArray): List<String>?

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    fun encodeByteArray(value: ByteArray?): ByteArray

    fun encodeByteArrayList(value: List<ByteArray>?): ByteArray

    fun decodeByteArray(source: ByteArray): ByteArray

    fun decodeByteArrayOrNull(source: ByteArray): ByteArray?

    fun decodeByteArrayList(source: ByteArray): List<ByteArray>

    fun decodeByteArrayListOrNull(source: ByteArray): List<ByteArray>?

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    fun encodeUuid(value: UUID<*>?): ByteArray

    fun encodeUuidList(value: List<UUID<*>>?): ByteArray

    fun decodeUuid(source: ByteArray): UUID<Any>

    fun decodeUuidOrNull(source: ByteArray): UUID<Any>?

    fun decodeUuidList(source: ByteArray): List<UUID<Any>>

    fun decodeUuidListOrNull(source: ByteArray): List<UUID<Any>>?

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    fun <T> encodeInstance(value: T?, wireFormat: WireFormat<T>): ByteArray

    fun <T> encodeInstanceList(value: List<T>?, wireFormat: WireFormat<T>): ByteArray

    fun <T> decodeInstance(source: ByteArray, wireFormat: WireFormat<T>): T

    fun <T> decodeInstanceOrNull(source: ByteArray, wireFormat: WireFormat<T>): T?

    fun <T> decodeInstanceList(source: ByteArray, wireFormat: WireFormat<T>): List<T>

    fun <T> decodeInstanceListOrNull(source: ByteArray, wireFormat: WireFormat<T>): List<T>?

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray

    fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray

    fun <E : Enum<E>> decodeEnum(source: ByteArray, entries: EnumEntries<E>): E

    fun <E : Enum<E>> decodeEnumOrNull(source: ByteArray, entries: EnumEntries<E>): E?

    fun <E : Enum<E>> decodeEnumList(source: ByteArray, entries: EnumEntries<E>): List<E>

    fun <E : Enum<E>> decodeEnumListOrNull(source: ByteArray, entries: EnumEntries<E>): List<E>?

    // ---------------------------------------------------------------------------
    // UInt
    // ---------------------------------------------------------------------------

    fun encodeUInt(value: UInt?): ByteArray

    fun encodeUIntList(value: List<UInt>?): ByteArray

    fun decodeUInt(source: ByteArray): UInt

    fun decodeUIntOrNull(source: ByteArray): UInt?

    fun decodeUIntList(source: ByteArray): List<UInt>

    fun decodeUIntListOrNull(source: ByteArray): List<UInt>?

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    fun encodeUShort(value: UShort?): ByteArray

    fun encodeUShortList(value: List<UShort>?): ByteArray

    fun decodeUShort(source: ByteArray): UShort

    fun decodeUShortOrNull(source: ByteArray): UShort?

    fun decodeUShortList(source: ByteArray): List<UShort>

    fun decodeUShortListOrNull(source: ByteArray): List<UShort>?

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    fun encodeUByte(value: UByte?): ByteArray

    fun encodeUByteList(value: List<UByte>?): ByteArray

    fun decodeUByte(source: ByteArray): UByte

    fun decodeUByteOrNull(source: ByteArray): UByte?

    fun decodeUByteList(source: ByteArray): List<UByte>

    fun decodeUByteListOrNull(source: ByteArray): List<UByte>?

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    fun encodeULong(value: ULong?): ByteArray

    fun encodeULongList(value: List<ULong>?): ByteArray

    fun decodeULong(source: ByteArray): ULong

    fun decodeULongOrNull(source: ByteArray): ULong?

    fun decodeULongList(source: ByteArray): List<ULong>

    fun decodeULongListOrNull(source: ByteArray): List<ULong>?

}