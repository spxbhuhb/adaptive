package hu.simplexion.z2.serialization.json

import hu.simplexion.z2.serialization.InstanceDecoder
import hu.simplexion.z2.serialization.StandaloneValue
import hu.simplexion.z2.util.UUID
import kotlin.enums.EnumEntries

object JsonStandaloneValue : StandaloneValue<JsonMessage> {

    override fun decodeUnit(message: JsonMessage?) {}

    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    override fun decodeBoolean(message: JsonMessage?): Boolean =
        message?.boolean(1, "") ?: false

    override fun decodeBooleanOrNull(message: JsonMessage?): Boolean? =
        message?.booleanOrNull(1, "")

    override fun decodeBooleanList(message: JsonMessage?): List<Boolean> =
        message?.booleanList(1, "") ?: emptyList()

    override fun decodeBooleanListOrNull(message: JsonMessage?): List<Boolean>? =
        message?.booleanListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    override fun decodeInt(message: JsonMessage?): Int =
        message?.int(1, "") ?: 0

    override fun decodeIntOrNull(message: JsonMessage?): Int? =
        message?.intOrNull(1, "")

    override fun decodeIntList(message: JsonMessage?): List<Int> =
        message?.intList(1, "") ?: emptyList()

    override fun decodeIntListOrNull(message: JsonMessage?): List<Int>? =
        message?.intListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    override fun decodeLong(message: JsonMessage?): Long =
        message?.long(1, "") ?: 0L

    override fun decodeLongOrNull(message: JsonMessage?): Long? =
        message?.longOrNull(1, "")

    override fun decodeLongList(message: JsonMessage?): List<Long> =
        message?.longList(1, "") ?: emptyList()

    override fun decodeLongListOrNull(message: JsonMessage?): List<Long>? =
        message?.longListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    override fun decodeString(message: JsonMessage?): String =
        message?.string(1, "") ?: ""

    override fun decodeStringOrNull(message: JsonMessage?): String? =
        message?.stringOrNull(1, "")

    override fun decodeStringList(message: JsonMessage?): List<String> =
        message?.stringList(1, "") ?: emptyList()

    override fun decodeStringListOrNull(message: JsonMessage?): List<String>? =
        message?.stringListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    override fun decodeByteArray(message: JsonMessage?): ByteArray =
        message?.byteArray(1, "") ?: ByteArray(0)

    override fun decodeByteArrayOrNull(message: JsonMessage?): ByteArray? =
        message?.byteArrayOrNull(1, "")

    override fun decodeByteArrayList(message: JsonMessage?): List<ByteArray> =
        message?.byteArrayList(1, "") ?: emptyList()

    override fun decodeByteArrayListOrNull(message: JsonMessage?): List<ByteArray>? =
        message?.byteArrayListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    override fun decodeUuid(message: JsonMessage?): UUID<Any> =
        message?.uuid(1, "") ?: UUID()

    override fun decodeUuidOrNull(message: JsonMessage?): UUID<Any>? =
        message?.uuidOrNull(1, "")

    override fun decodeUuidList(message: JsonMessage?): List<UUID<Any>> =
        message?.uuidList(1, "") ?: emptyList()

    override fun decodeUuidListOrNull(message: JsonMessage?): List<UUID<Any>>? =
        message?.uuidListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    override fun <T> decodeInstance(message: JsonMessage?, decoder: InstanceDecoder<T>): T =
        checkNotNull(message?.instance(1, "", decoder)) { "cannot decode instance with $decoder" }

    override fun <T> decodeInstanceOrNull(message: JsonMessage?, decoder: InstanceDecoder<T>): T? =
        message?.instanceOrNull(1, "", decoder)

    override fun <T> decodeInstanceList(message: JsonMessage?, decoder: InstanceDecoder<T>): List<T> =
        message?.instanceList(1, "", decoder) ?: emptyList()

    override fun <T> decodeInstanceListOrNull(message: JsonMessage?, decoder: InstanceDecoder<T>): List<T>? =
        message?.instanceListOrNull(1, "", decoder)

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    override fun <E : Enum<E>> decodeEnum(message: JsonMessage?, entries: EnumEntries<E>): E {
        if (message == null) return entries.first()
        return entries[message.int(1, "")]
    }

    override fun <E : Enum<E>> decodeEnumOrNull(message: JsonMessage?, entries: EnumEntries<E>): E? {
        if (message == null) return null
        return message.intOrNull(1, "")?.let { entries[it] }
    }

    override fun <E : Enum<E>> decodeEnumList(message: JsonMessage?, entries: EnumEntries<E>): List<E> {
        if (message == null) return emptyList()
        return message.intList(1, "").map { entries[it] }
    }

    override fun <E : Enum<E>> decodeEnumListOrNull(message: JsonMessage?, entries: EnumEntries<E>): List<E>? {
        if (message == null) return null
        return message.intListOrNull(1, "")?.map { entries[it] }
    }
}
