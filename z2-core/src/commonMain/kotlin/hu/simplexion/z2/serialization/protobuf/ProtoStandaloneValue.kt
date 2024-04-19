package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.serialization.InstanceDecoder
import hu.simplexion.z2.serialization.InstanceEncoder
import hu.simplexion.z2.serialization.StandaloneValue
import hu.simplexion.z2.util.UUID
import kotlin.enums.EnumEntries

object ProtoStandaloneValue : StandaloneValue<ProtoMessage> {

    override fun decodeUnit(message: ProtoMessage?) {}

    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    override fun encodeBoolean(value: Boolean?): ByteArray =
        ProtoMessageBuilder().booleanOrNull(1, "", value).pack()

    override fun encodeBooleanList(value: List<Boolean>?): ByteArray =
        ProtoMessageBuilder().booleanListOrNull(1, "", value).pack()

    override fun decodeBoolean(message: ProtoMessage?): Boolean =
        message?.boolean(1, "") ?: false

    override fun decodeBooleanOrNull(message: ProtoMessage?): Boolean? =
        message?.booleanOrNull(1, "")

    override fun decodeBooleanList(message: ProtoMessage?): List<Boolean> =
        message?.booleanList(1, "") ?: emptyList()

    override fun decodeBooleanListOrNull(message: ProtoMessage?): List<Boolean>? =
        message?.booleanListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    override fun encodeInt(value: Int?): ByteArray =
        ProtoMessageBuilder().intOrNull(1, "", value).pack()

    override fun encodeIntList(value: List<Int>?): ByteArray =
        ProtoMessageBuilder().intListOrNull(1, "", value).pack()

    override fun decodeInt(message: ProtoMessage?): Int =
        message?.int(1, "") ?: 0

    override fun decodeIntOrNull(message: ProtoMessage?): Int? =
        message?.intOrNull(1, "")

    override fun decodeIntList(message: ProtoMessage?): List<Int> =
        message?.intList(1, "") ?: emptyList()

    override fun decodeIntListOrNull(message: ProtoMessage?): List<Int>? =
        message?.intListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    override fun encodeLong(value: Long?): ByteArray =
        ProtoMessageBuilder().longOrNull(1, "", value).pack()

    override fun encodeLongList(value: List<Long>?): ByteArray =
        ProtoMessageBuilder().longListOrNull(1, "", value).pack()

    override fun decodeLong(message: ProtoMessage?): Long =
        message?.long(1, "") ?: 0L

    override fun decodeLongOrNull(message: ProtoMessage?): Long? =
        message?.longOrNull(1, "")

    override fun decodeLongList(message: ProtoMessage?): List<Long> =
        message?.longList(1, "") ?: emptyList()

    override fun decodeLongListOrNull(message: ProtoMessage?): List<Long>? =
        message?.longListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    override fun encodeString(value: String?): ByteArray =
        ProtoMessageBuilder().stringOrNull(1, "", value).pack()

    override fun encodeStringList(value: List<String>?): ByteArray =
        ProtoMessageBuilder().stringListOrNull(1, "", value).pack()

    override fun decodeString(message: ProtoMessage?): String =
        message?.string(1, "") ?: ""

    override fun decodeStringOrNull(message: ProtoMessage?): String? =
        message?.stringOrNull(1, "")

    override fun decodeStringList(message: ProtoMessage?): List<String> =
        message?.stringList(1, "") ?: emptyList()

    override fun decodeStringListOrNull(message: ProtoMessage?): List<String>? =
        message?.stringListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    override fun encodeByteArray(value: ByteArray?): ByteArray =
        ProtoMessageBuilder().byteArrayOrNull(1, "", value).pack()

    override fun encodeByteArrayList(value: List<ByteArray>?): ByteArray =
        ProtoMessageBuilder().byteArrayListOrNull(1, "", value).pack()

    override fun decodeByteArray(message: ProtoMessage?): ByteArray =
        message?.byteArray(1, "") ?: ByteArray(0)

    override fun decodeByteArrayOrNull(message: ProtoMessage?): ByteArray? =
        message?.byteArrayOrNull(1, "")

    override fun decodeByteArrayList(message: ProtoMessage?): List<ByteArray> =
        message?.byteArrayList(1, "") ?: emptyList()

    override fun decodeByteArrayListOrNull(message: ProtoMessage?): List<ByteArray>? =
        message?.byteArrayListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    override fun encodeUuid(value: UUID<*>?): ByteArray =
        ProtoMessageBuilder().uuidOrNull(1, "", value).pack()

    override fun encodeUuidList(value: List<UUID<*>>?): ByteArray =
        ProtoMessageBuilder().uuidListOrNull(1, "", value).pack()

    override fun decodeUuid(message: ProtoMessage?): UUID<Any> =
        message?.uuid(1, "") ?: UUID()

    override fun decodeUuidOrNull(message: ProtoMessage?): UUID<Any>? =
        message?.uuidOrNull(1, "")

    override fun decodeUuidList(message: ProtoMessage?): List<UUID<Any>> =
        message?.uuidList(1, "") ?: emptyList()

    override fun decodeUuidListOrNull(message: ProtoMessage?): List<UUID<Any>>? =
        message?.uuidListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    override fun <T> encodeInstance(value: T?, encoder: InstanceEncoder<T>): ByteArray =
        ProtoMessageBuilder().instanceOrNull(1, "", encoder, value).pack()

    override fun <T> encodeInstanceList(value: List<T>?, encoder: InstanceEncoder<T>): ByteArray =
        ProtoMessageBuilder().instanceListOrNull(1, "", encoder, value).pack()

    override fun <T> decodeInstance(message: ProtoMessage?, decoder: InstanceDecoder<T>): T =
        checkNotNull(message?.instance(1, "", decoder)) { "cannot decode instance with $decoder" }

    override fun <T> decodeInstanceOrNull(message: ProtoMessage?, decoder: InstanceDecoder<T>): T? =
        message?.instanceOrNull(1, "", decoder)

    override fun <T> decodeInstanceList(message: ProtoMessage?, decoder: InstanceDecoder<T>): List<T> =
        message?.instanceList(1, "", decoder) ?: emptyList()

    override fun <T> decodeInstanceListOrNull(message: ProtoMessage?, decoder: InstanceDecoder<T>): List<T>? =
        message?.instanceListOrNull(1, "", decoder)

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    override fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray =
        ProtoMessageBuilder().enumOrNull(1, "", entries, value).pack()

    override fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray =
        ProtoMessageBuilder().enumListOrNull(1, "", entries, value).pack()

    override fun <E : Enum<E>> decodeEnum(message: ProtoMessage?, entries: EnumEntries<E>): E {
        if (message == null) return entries.first()
        return entries[message.int(1, "")]
    }

    override fun <E : Enum<E>> decodeEnumOrNull(message: ProtoMessage?, entries: EnumEntries<E>): E? {
        if (message == null) return null
        return message.intOrNull(1, "")?.let { entries[it] }
    }

    override fun <E : Enum<E>> decodeEnumList(message: ProtoMessage?, entries: EnumEntries<E>): List<E> {
        if (message == null) return emptyList()
        return message.intList(1, "").map { entries[it] }
    }

    override fun <E : Enum<E>> decodeEnumListOrNull(message: ProtoMessage?, entries: EnumEntries<E>): List<E>? {
        if (message == null) return null
        return message.intListOrNull(1, "")?.map { entries[it] }
    }

}
