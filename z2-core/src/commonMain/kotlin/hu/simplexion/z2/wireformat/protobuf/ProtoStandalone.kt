package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormat
import kotlin.enums.EnumEntries

object ProtoStandalone : Standalone<ProtoMessage> {

    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    override fun encodeAny(value: Any?): ByteArray =
        TODO()

    override fun encodeAnyList(value: List<Any>?): ByteArray =
        TODO()

    override fun decodeAny(message: ProtoMessage?): Any =
        TODO()

    override fun decodeAnyOrNull(message: ProtoMessage?): Any =
        TODO()

    override fun decodeAnyList(message: ProtoMessage?): List<Any> =
        TODO()

    override fun decodeAnyListOrNull(message: ProtoMessage?): List<Any> =
        TODO()

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    override fun encodeUnit(value: Unit?): ByteArray =
        ProtoMessageBuilder().unitOrNull(1, "", value).pack()

    override fun encodeUnitList(value: List<Unit>?): ByteArray =
        ProtoMessageBuilder().unitListOrNull(1, "", value).pack()

    override fun decodeUnit(message: ProtoMessage?): Unit =
        checkNotNull(message?.unit(1, ""))

    override fun decodeUnitOrNull(message: ProtoMessage?): Unit? =
        message?.unitOrNull(1, "")

    override fun decodeUnitList(message: ProtoMessage?): List<Unit> =
        message?.unitList(1, "") ?: emptyList()

    override fun decodeUnitListOrNull(message: ProtoMessage?): List<Unit>? =
        message?.unitListOrNull(1, "")
    
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
    // Short
    // ---------------------------------------------------------------------------

    override fun encodeShort(value: Short?): ByteArray =
        ProtoMessageBuilder().shortOrNull(1, "", value).pack()

    override fun encodeShortList(value: List<Short>?): ByteArray =
        ProtoMessageBuilder().shortListOrNull(1, "", value).pack()

    override fun decodeShort(message: ProtoMessage?): Short =
        message?.short(1, "") ?: 0

    override fun decodeShortOrNull(message: ProtoMessage?): Short? =
        message?.shortOrNull(1, "")

    override fun decodeShortList(message: ProtoMessage?): List<Short> =
        message?.shortList(1, "") ?: emptyList()

    override fun decodeShortListOrNull(message: ProtoMessage?): List<Short>? =
        message?.shortListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    override fun encodeByte(value: Byte?): ByteArray =
        ProtoMessageBuilder().byteOrNull(1, "", value).pack()

    override fun encodeByteList(value: List<Byte>?): ByteArray =
        ProtoMessageBuilder().byteListOrNull(1, "", value).pack()

    override fun decodeByte(message: ProtoMessage?): Byte =
        message?.byte(1, "") ?: 0

    override fun decodeByteOrNull(message: ProtoMessage?): Byte? =
        message?.byteOrNull(1, "")

    override fun decodeByteList(message: ProtoMessage?): List<Byte> =
        message?.byteList(1, "") ?: emptyList()

    override fun decodeByteListOrNull(message: ProtoMessage?): List<Byte>? =
        message?.byteListOrNull(1, "")

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
    // Float
    // ---------------------------------------------------------------------------

    override fun encodeFloat(value: Float?): ByteArray =
        ProtoMessageBuilder().floatOrNull(1, "", value).pack()

    override fun encodeFloatList(value: List<Float>?): ByteArray =
        ProtoMessageBuilder().floatListOrNull(1, "", value).pack()

    override fun decodeFloat(message: ProtoMessage?): Float =
        message?.float(1, "") ?: 0f

    override fun decodeFloatOrNull(message: ProtoMessage?): Float? =
        message?.floatOrNull(1, "")

    override fun decodeFloatList(message: ProtoMessage?): List<Float> =
        message?.floatList(1, "") ?: emptyList()

    override fun decodeFloatListOrNull(message: ProtoMessage?): List<Float>? =
        message?.floatListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    override fun encodeDouble(value: Double?): ByteArray =
        ProtoMessageBuilder().doubleOrNull(1, "", value).pack()

    override fun encodeDoubleList(value: List<Double>?): ByteArray =
        ProtoMessageBuilder().doubleListOrNull(1, "", value).pack()

    override fun decodeDouble(message: ProtoMessage?): Double =
        message?.double(1, "") ?: 0.0

    override fun decodeDoubleOrNull(message: ProtoMessage?): Double? =
        message?.doubleOrNull(1, "")

    override fun decodeDoubleList(message: ProtoMessage?): List<Double> =
        message?.doubleList(1, "") ?: emptyList()

    override fun decodeDoubleListOrNull(message: ProtoMessage?): List<Double>? =
        message?.doubleListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    override fun encodeChar(value: Char?): ByteArray =
        ProtoMessageBuilder().charOrNull(1, "", value).pack()

    override fun encodeCharList(value: List<Char>?): ByteArray =
        ProtoMessageBuilder().charListOrNull(1, "", value).pack()

    override fun decodeChar(message: ProtoMessage?): Char =
        message?.char(1, "") ?: Char.MIN_VALUE

    override fun decodeCharOrNull(message: ProtoMessage?): Char? =
        message?.charOrNull(1, "")

    override fun decodeCharList(message: ProtoMessage?): List<Char> =
        message?.charList(1, "") ?: emptyList()

    override fun decodeCharListOrNull(message: ProtoMessage?): List<Char>? =
        message?.charListOrNull(1, "")
    
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

    override fun <T> encodeInstance(value: T?, encoder: WireFormat<T>): ByteArray =
        ProtoMessageBuilder().instanceOrNull(1, "", encoder, value).pack()

    override fun <T> encodeInstanceList(value: List<T>?, encoder: WireFormat<T>): ByteArray =
        ProtoMessageBuilder().instanceListOrNull(1, "", encoder, value).pack()

    override fun <T> decodeInstance(message: ProtoMessage?, decoder: WireFormat<T>): T =
        checkNotNull(message?.instance(1, "", decoder)) { "cannot decode instance with $decoder" }

    override fun <T> decodeInstanceOrNull(message: ProtoMessage?, decoder: WireFormat<T>): T? =
        message?.instanceOrNull(1, "", decoder)

    override fun <T> decodeInstanceList(message: ProtoMessage?, decoder: WireFormat<T>): List<T> =
        message?.instanceList(1, "", decoder) ?: emptyList()

    override fun <T> decodeInstanceListOrNull(message: ProtoMessage?, decoder: WireFormat<T>): List<T>? =
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

    // ---------------------------------------------------------------------------
    // UInt
    // ---------------------------------------------------------------------------

    override fun encodeUInt(value: UInt?): ByteArray =
        ProtoMessageBuilder().uIntOrNull(1, "", value).pack()

    override fun encodeUIntList(value: List<UInt>?): ByteArray =
        ProtoMessageBuilder().uIntListOrNull(1, "", value).pack()

    override fun decodeUInt(message: ProtoMessage?): UInt =
        message?.uInt(1, "") ?: UInt.MIN_VALUE

    override fun decodeUIntOrNull(message: ProtoMessage?): UInt? =
        message?.uIntOrNull(1, "")

    override fun decodeUIntList(message: ProtoMessage?): List<UInt> =
        message?.uIntList(1, "") ?: emptyList()

    override fun decodeUIntListOrNull(message: ProtoMessage?): List<UInt>? =
        message?.uIntListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    override fun encodeUShort(value: UShort?): ByteArray =
        ProtoMessageBuilder().uShortOrNull(1, "", value).pack()

    override fun encodeUShortList(value: List<UShort>?): ByteArray =
        ProtoMessageBuilder().uShortListOrNull(1, "", value).pack()

    override fun decodeUShort(message: ProtoMessage?): UShort =
        message?.uShort(1, "") ?: UShort.MIN_VALUE

    override fun decodeUShortOrNull(message: ProtoMessage?): UShort? =
        message?.uShortOrNull(1, "")

    override fun decodeUShortList(message: ProtoMessage?): List<UShort> =
        message?.uShortList(1, "") ?: emptyList()

    override fun decodeUShortListOrNull(message: ProtoMessage?): List<UShort>? =
        message?.uShortListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    override fun encodeUByte(value: UByte?): ByteArray =
        ProtoMessageBuilder().uByteOrNull(1, "", value).pack()

    override fun encodeUByteList(value: List<UByte>?): ByteArray =
        ProtoMessageBuilder().uByteListOrNull(1, "", value).pack()

    override fun decodeUByte(message: ProtoMessage?): UByte =
        message?.uByte(1, "") ?: UByte.MIN_VALUE

    override fun decodeUByteOrNull(message: ProtoMessage?): UByte? =
        message?.uByteOrNull(1, "")

    override fun decodeUByteList(message: ProtoMessage?): List<UByte> =
        message?.uByteList(1, "") ?: emptyList()

    override fun decodeUByteListOrNull(message: ProtoMessage?): List<UByte>? =
        message?.uByteListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    override fun encodeULong(value: ULong?): ByteArray =
        ProtoMessageBuilder().uLongOrNull(1, "", value).pack()

    override fun encodeULongList(value: List<ULong>?): ByteArray =
        ProtoMessageBuilder().uLongListOrNull(1, "", value).pack()

    override fun decodeULong(message: ProtoMessage?): ULong =
        message?.uLong(1, "") ?: ULong.MIN_VALUE

    override fun decodeULongOrNull(message: ProtoMessage?): ULong? =
        message?.uLongOrNull(1, "")

    override fun decodeULongList(message: ProtoMessage?): List<ULong> =
        message?.uLongList(1, "") ?: emptyList()

    override fun decodeULongListOrNull(message: ProtoMessage?): List<ULong>? =
        message?.uLongListOrNull(1, "")

}
