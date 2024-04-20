package hu.simplexion.z2.wireformat

import hu.simplexion.z2.util.UUID
import kotlin.enums.EnumEntries

interface Standalone<M> {

    fun decodeUnit(message: M?)

    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    fun encodeBoolean(value: Boolean?): ByteArray

    fun encodeBooleanList(value: List<Boolean>?): ByteArray

    fun decodeBoolean(message: M?): Boolean

    fun decodeBooleanOrNull(message: M?): Boolean?

    fun decodeBooleanList(message: M?): List<Boolean>

    fun decodeBooleanListOrNull(message: M?): List<Boolean>?

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    fun encodeInt(value: Int?): ByteArray

    fun encodeIntList(value: List<Int>?): ByteArray

    fun decodeInt(message: M?): Int

    fun decodeIntOrNull(message: M?): Int?

    fun decodeIntList(message: M?): List<Int>

    fun decodeIntListOrNull(message: M?): List<Int>?

    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    fun encodeLong(value: Long?): ByteArray

    fun encodeLongList(value: List<Long>?): ByteArray

    fun decodeLong(message: M?): Long

    fun decodeLongOrNull(message: M?): Long?

    fun decodeLongList(message: M?): List<Long>

    fun decodeLongListOrNull(message: M?): List<Long>?

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    fun encodeFloat(value: Float?): ByteArray

    fun encodeFloatList(value: List<Float>?): ByteArray

    fun decodeFloat(message: M?): Float

    fun decodeFloatOrNull(message: M?): Float?

    fun decodeFloatList(message: M?): List<Float>

    fun decodeFloatListOrNull(message: M?): List<Float>?

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    fun encodeDouble(value: Double?): ByteArray

    fun encodeDoubleList(value: List<Double>?): ByteArray

    fun decodeDouble(message: M?): Double

    fun decodeDoubleOrNull(message: M?): Double?

    fun decodeDoubleList(message: M?): List<Double>

    fun decodeDoubleListOrNull(message: M?): List<Double>?

    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    fun encodeString(value: String?): ByteArray

    fun encodeStringList(value: List<String>?): ByteArray

    fun decodeString(message: M?): String

    fun decodeStringOrNull(message: M?): String?

    fun decodeStringList(message: M?): List<String>

    fun decodeStringListOrNull(message: M?): List<String>?

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    fun encodeByteArray(value: ByteArray?): ByteArray

    fun encodeByteArrayList(value: List<ByteArray>?): ByteArray

    fun decodeByteArray(message: M?): ByteArray

    fun decodeByteArrayOrNull(message: M?): ByteArray?

    fun decodeByteArrayList(message: M?): List<ByteArray>

    fun decodeByteArrayListOrNull(message: M?): List<ByteArray>?

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    fun encodeUuid(value: UUID<*>?): ByteArray

    fun encodeUuidList(value: List<UUID<*>>?): ByteArray

    fun decodeUuid(message: M?): UUID<Any>

    fun decodeUuidOrNull(message: M?): UUID<Any>?

    fun decodeUuidList(message: M?): List<UUID<Any>>

    fun decodeUuidListOrNull(message: M?): List<UUID<Any>>?

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    fun <T> encodeInstance(value: T?, encoder: WireFormat<T>): ByteArray

    fun <T> encodeInstanceList(value: List<T>?, encoder: WireFormat<T>): ByteArray

    fun <T> decodeInstance(message: M?, decoder: WireFormat<T>): T

    fun <T> decodeInstanceOrNull(message: M?, decoder: WireFormat<T>): T?

    fun <T> decodeInstanceList(message: M?, decoder: WireFormat<T>): List<T>

    fun <T> decodeInstanceListOrNull(message: M?, decoder: WireFormat<T>): List<T>?

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray

    fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray

    fun <E : Enum<E>> decodeEnum(message: M?, entries: EnumEntries<E>): E

    fun <E : Enum<E>> decodeEnumOrNull(message: M?, entries: EnumEntries<E>): E?

    fun <E : Enum<E>> decodeEnumList(message: M?, entries: EnumEntries<E>): List<E>

    fun <E : Enum<E>> decodeEnumListOrNull(message: M?, entries: EnumEntries<E>): List<E>?

}