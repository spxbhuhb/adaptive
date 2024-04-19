package hu.simplexion.z2.serialization

import hu.simplexion.z2.util.UUID
import kotlin.enums.EnumEntries

interface StandaloneValue<M> {

    fun decodeUnit(message: M?)

    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    fun decodeBoolean(message: M?): Boolean

    fun decodeBooleanOrNull(message: M?): Boolean?

    fun decodeBooleanList(message: M?): List<Boolean>

    fun decodeBooleanListOrNull(message: M?): List<Boolean>?

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    fun decodeInt(message: M?): Int

    fun decodeIntOrNull(message: M?): Int?

    fun decodeIntList(message: M?): List<Int>

    fun decodeIntListOrNull(message: M?): List<Int>?

    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    fun decodeLong(message: M?): Long

    fun decodeLongOrNull(message: M?): Long?

    fun decodeLongList(message: M?): List<Long>

    fun decodeLongListOrNull(message: M?): List<Long>?

    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    fun decodeString(message: M?): String

    fun decodeStringOrNull(message: M?): String?

    fun decodeStringList(message: M?): List<String>

    fun decodeStringListOrNull(message: M?): List<String>?

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    fun decodeByteArray(message: M?): ByteArray

    fun decodeByteArrayOrNull(message: M?): ByteArray?

    fun decodeByteArrayList(message: M?): List<ByteArray>

    fun decodeByteArrayListOrNull(message: M?): List<ByteArray>?

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    fun decodeUuid(message: M?): UUID<Any>

    fun decodeUuidOrNull(message: M?): UUID<Any>?

    fun decodeUuidList(message: M?): List<UUID<Any>>

    fun decodeUuidListOrNull(message: M?): List<UUID<Any>>?

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    fun <T> decodeInstance(message: M?, decoder: InstanceDecoder<T>): T

    fun <T> decodeInstanceOrNull(message: M?, decoder: InstanceDecoder<T>): T?

    fun <T> decodeInstanceList(message: M?, decoder: InstanceDecoder<T>): List<T>

    fun <T> decodeInstanceListOrNull(message: M?, decoder: InstanceDecoder<T>): List<T>?

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    fun <E : Enum<E>> decodeEnum(message: M?, entries: EnumEntries<E>): E

    fun <E : Enum<E>> decodeEnumOrNull(message: M?, entries: EnumEntries<E>): E?

    fun <E : Enum<E>> decodeEnumList(message: M?, entries: EnumEntries<E>): List<E>

    fun <E : Enum<E>> decodeEnumListOrNull(message: M?, entries: EnumEntries<E>): List<E>?

}