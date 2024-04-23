package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
import kotlin.enums.EnumEntries

interface Standalone<D> {

    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    fun encodeAny(value: Any?): ByteArray

    fun encodeAnyList(value: List<Any>?): ByteArray

    fun decodeAny(decoder: D?): Any

    fun decodeAnyOrNull(decoder: D?): Any?

    fun decodeAnyList(decoder: D?): List<Any>

    fun decodeAnyListOrNull(decoder: D?): List<Any>?

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    fun encodeUnit(value: Unit?): ByteArray

    fun encodeUnitList(value: List<Unit>?): ByteArray

    fun decodeUnit(decoder: D?)

    fun decodeUnitOrNull(decoder: D?): Unit?

    fun decodeUnitList(decoder: D?): List<Unit>

    fun decodeUnitListOrNull(decoder: D?): List<Unit>?
    
    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    fun encodeBoolean(value: Boolean?): ByteArray

    fun encodeBooleanList(value: List<Boolean>?): ByteArray

    fun decodeBoolean(decoder: D?): Boolean

    fun decodeBooleanOrNull(decoder: D?): Boolean?

    fun decodeBooleanList(decoder: D?): List<Boolean>

    fun decodeBooleanListOrNull(decoder: D?): List<Boolean>?

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    fun encodeInt(value: Int?): ByteArray

    fun encodeIntList(value: List<Int>?): ByteArray

    fun decodeInt(decoder: D?): Int

    fun decodeIntOrNull(decoder: D?): Int?

    fun decodeIntList(decoder: D?): List<Int>

    fun decodeIntListOrNull(decoder: D?): List<Int>?

    // ---------------------------------------------------------------------------
    // Short
    // ---------------------------------------------------------------------------

    fun encodeShort(value: Short?): ByteArray

    fun encodeShortList(value: List<Short>?): ByteArray

    fun decodeShort(decoder: D?): Short

    fun decodeShortOrNull(decoder: D?): Short?

    fun decodeShortList(decoder: D?): List<Short>

    fun decodeShortListOrNull(decoder: D?): List<Short>?

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    fun encodeByte(value: Byte?): ByteArray

    fun encodeByteList(value: List<Byte>?): ByteArray

    fun decodeByte(decoder: D?): Byte

    fun decodeByteOrNull(decoder: D?): Byte?

    fun decodeByteList(decoder: D?): List<Byte>

    fun decodeByteListOrNull(decoder: D?): List<Byte>?
    
    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    fun encodeLong(value: Long?): ByteArray

    fun encodeLongList(value: List<Long>?): ByteArray

    fun decodeLong(decoder: D?): Long

    fun decodeLongOrNull(decoder: D?): Long?

    fun decodeLongList(decoder: D?): List<Long>

    fun decodeLongListOrNull(decoder: D?): List<Long>?

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    fun encodeFloat(value: Float?): ByteArray

    fun encodeFloatList(value: List<Float>?): ByteArray

    fun decodeFloat(decoder: D?): Float

    fun decodeFloatOrNull(decoder: D?): Float?

    fun decodeFloatList(decoder: D?): List<Float>

    fun decodeFloatListOrNull(decoder: D?): List<Float>?

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    fun encodeDouble(value: Double?): ByteArray

    fun encodeDoubleList(value: List<Double>?): ByteArray

    fun decodeDouble(decoder: D?): Double

    fun decodeDoubleOrNull(decoder: D?): Double?

    fun decodeDoubleList(decoder: D?): List<Double>

    fun decodeDoubleListOrNull(decoder: D?): List<Double>?

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    fun encodeChar(value: Char?): ByteArray

    fun encodeCharList(value: List<Char>?): ByteArray

    fun decodeChar(decoder: D?): Char

    fun decodeCharOrNull(decoder: D?): Char?

    fun decodeCharList(decoder: D?): List<Char>

    fun decodeCharListOrNull(decoder: D?): List<Char>?
    
    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    fun encodeString(value: String?): ByteArray

    fun encodeStringList(value: List<String>?): ByteArray

    fun decodeString(decoder: D?): String

    fun decodeStringOrNull(decoder: D?): String?

    fun decodeStringList(decoder: D?): List<String>

    fun decodeStringListOrNull(decoder: D?): List<String>?

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    fun encodeByteArray(value: ByteArray?): ByteArray

    fun encodeByteArrayList(value: List<ByteArray>?): ByteArray

    fun decodeByteArray(decoder: D?): ByteArray

    fun decodeByteArrayOrNull(decoder: D?): ByteArray?

    fun decodeByteArrayList(decoder: D?): List<ByteArray>

    fun decodeByteArrayListOrNull(decoder: D?): List<ByteArray>?

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    fun encodeUuid(value: UUID<*>?): ByteArray

    fun encodeUuidList(value: List<UUID<*>>?): ByteArray

    fun decodeUuid(decoder: D?): UUID<Any>

    fun decodeUuidOrNull(decoder: D?): UUID<Any>?

    fun decodeUuidList(decoder: D?): List<UUID<Any>>

    fun decodeUuidListOrNull(decoder: D?): List<UUID<Any>>?

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    fun <T> encodeInstance(value: T?, wireFormat: WireFormat<T>): ByteArray

    fun <T> encodeInstanceList(value: List<T>?, wireFormat: WireFormat<T>): ByteArray

    fun <T> decodeInstance(decoder: D?, wireFormat: WireFormat<T>): T

    fun <T> decodeInstanceOrNull(decoder: D?, wireFormat: WireFormat<T>): T?

    fun <T> decodeInstanceList(decoder: D?, wireFormat: WireFormat<T>): List<T>

    fun <T> decodeInstanceListOrNull(decoder: D?, wireFormat: WireFormat<T>): List<T>?

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray

    fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray

    fun <E : Enum<E>> decodeEnum(decoder: D?, entries: EnumEntries<E>): E

    fun <E : Enum<E>> decodeEnumOrNull(decoder: D?, entries: EnumEntries<E>): E?

    fun <E : Enum<E>> decodeEnumList(decoder: D?, entries: EnumEntries<E>): List<E>

    fun <E : Enum<E>> decodeEnumListOrNull(decoder: D?, entries: EnumEntries<E>): List<E>?

    // ---------------------------------------------------------------------------
    // UInt
    // ---------------------------------------------------------------------------

    fun encodeUInt(value: UInt?): ByteArray

    fun encodeUIntList(value: List<UInt>?): ByteArray

    fun decodeUInt(decoder: D?): UInt

    fun decodeUIntOrNull(decoder: D?): UInt?

    fun decodeUIntList(decoder: D?): List<UInt>

    fun decodeUIntListOrNull(decoder: D?): List<UInt>?

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    fun encodeUShort(value: UShort?): ByteArray

    fun encodeUShortList(value: List<UShort>?): ByteArray

    fun decodeUShort(decoder: D?): UShort

    fun decodeUShortOrNull(decoder: D?): UShort?

    fun decodeUShortList(decoder: D?): List<UShort>

    fun decodeUShortListOrNull(decoder: D?): List<UShort>?

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    fun encodeUByte(value: UByte?): ByteArray

    fun encodeUByteList(value: List<UByte>?): ByteArray

    fun decodeUByte(decoder: D?): UByte

    fun decodeUByteOrNull(decoder: D?): UByte?

    fun decodeUByteList(decoder: D?): List<UByte>

    fun decodeUByteListOrNull(decoder: D?): List<UByte>?

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    fun encodeULong(value: ULong?): ByteArray

    fun encodeULongList(value: List<ULong>?): ByteArray

    fun decodeULong(decoder: D?): ULong

    fun decodeULongOrNull(decoder: D?): ULong?

    fun decodeULongList(decoder: D?): List<ULong>

    fun decodeULongListOrNull(decoder: D?): List<ULong>?

}