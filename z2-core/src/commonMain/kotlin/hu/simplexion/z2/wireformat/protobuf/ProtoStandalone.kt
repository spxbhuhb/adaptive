package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormat
import kotlin.enums.EnumEntries

object ProtoStandalone : Standalone<ProtoWireFormatDecoder> {

    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    override fun encodeAny(value: Any?): ByteArray =
        TODO()

    override fun encodeAnyList(value: List<Any>?): ByteArray =
        TODO()

    override fun decodeAny(message: ProtoWireFormatDecoder?): Any =
        TODO()

    override fun decodeAnyOrNull(message: ProtoWireFormatDecoder?): Any =
        TODO()

    override fun decodeAnyList(message: ProtoWireFormatDecoder?): List<Any> =
        TODO()

    override fun decodeAnyListOrNull(message: ProtoWireFormatDecoder?): List<Any> =
        TODO()

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    override fun encodeUnit(value: Unit?): ByteArray =
        ProtoWireFormatEncoder().unitOrNull(1, "", value).pack()

    override fun encodeUnitList(value: List<Unit>?): ByteArray =
        ProtoWireFormatEncoder().unitListOrNull(1, "", value).pack()

    override fun decodeUnit(message: ProtoWireFormatDecoder?): Unit =
        checkNotNull(message?.unit(1, ""))

    override fun decodeUnitOrNull(message: ProtoWireFormatDecoder?): Unit? =
        message?.unitOrNull(1, "")

    override fun decodeUnitList(message: ProtoWireFormatDecoder?): List<Unit> =
        message?.unitList(1, "") ?: emptyList()

    override fun decodeUnitListOrNull(message: ProtoWireFormatDecoder?): List<Unit>? =
        message?.unitListOrNull(1, "")
    
    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    override fun encodeBoolean(value: Boolean?): ByteArray =
        ProtoWireFormatEncoder().booleanOrNull(1, "", value).pack()

    override fun encodeBooleanList(value: List<Boolean>?): ByteArray =
        ProtoWireFormatEncoder().booleanListOrNull(1, "", value).pack()

    override fun decodeBoolean(message: ProtoWireFormatDecoder?): Boolean =
        message?.boolean(1, "") ?: false

    override fun decodeBooleanOrNull(message: ProtoWireFormatDecoder?): Boolean? =
        message?.booleanOrNull(1, "")

    override fun decodeBooleanList(message: ProtoWireFormatDecoder?): List<Boolean> =
        message?.booleanList(1, "") ?: emptyList()

    override fun decodeBooleanListOrNull(message: ProtoWireFormatDecoder?): List<Boolean>? =
        message?.booleanListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    override fun encodeInt(value: Int?): ByteArray =
        ProtoWireFormatEncoder().intOrNull(1, "", value).pack()

    override fun encodeIntList(value: List<Int>?): ByteArray =
        ProtoWireFormatEncoder().intListOrNull(1, "", value).pack()

    override fun decodeInt(message: ProtoWireFormatDecoder?): Int =
        message?.int(1, "") ?: 0

    override fun decodeIntOrNull(message: ProtoWireFormatDecoder?): Int? =
        message?.intOrNull(1, "")

    override fun decodeIntList(message: ProtoWireFormatDecoder?): List<Int> =
        message?.intList(1, "") ?: emptyList()

    override fun decodeIntListOrNull(message: ProtoWireFormatDecoder?): List<Int>? =
        message?.intListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Short
    // ---------------------------------------------------------------------------

    override fun encodeShort(value: Short?): ByteArray =
        ProtoWireFormatEncoder().shortOrNull(1, "", value).pack()

    override fun encodeShortList(value: List<Short>?): ByteArray =
        ProtoWireFormatEncoder().shortListOrNull(1, "", value).pack()

    override fun decodeShort(message: ProtoWireFormatDecoder?): Short =
        message?.short(1, "") ?: 0

    override fun decodeShortOrNull(message: ProtoWireFormatDecoder?): Short? =
        message?.shortOrNull(1, "")

    override fun decodeShortList(message: ProtoWireFormatDecoder?): List<Short> =
        message?.shortList(1, "") ?: emptyList()

    override fun decodeShortListOrNull(message: ProtoWireFormatDecoder?): List<Short>? =
        message?.shortListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    override fun encodeByte(value: Byte?): ByteArray =
        ProtoWireFormatEncoder().byteOrNull(1, "", value).pack()

    override fun encodeByteList(value: List<Byte>?): ByteArray =
        ProtoWireFormatEncoder().byteListOrNull(1, "", value).pack()

    override fun decodeByte(message: ProtoWireFormatDecoder?): Byte =
        message?.byte(1, "") ?: 0

    override fun decodeByteOrNull(message: ProtoWireFormatDecoder?): Byte? =
        message?.byteOrNull(1, "")

    override fun decodeByteList(message: ProtoWireFormatDecoder?): List<Byte> =
        message?.byteList(1, "") ?: emptyList()

    override fun decodeByteListOrNull(message: ProtoWireFormatDecoder?): List<Byte>? =
        message?.byteListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    override fun encodeLong(value: Long?): ByteArray =
        ProtoWireFormatEncoder().longOrNull(1, "", value).pack()

    override fun encodeLongList(value: List<Long>?): ByteArray =
        ProtoWireFormatEncoder().longListOrNull(1, "", value).pack()

    override fun decodeLong(message: ProtoWireFormatDecoder?): Long =
        message?.long(1, "") ?: 0L

    override fun decodeLongOrNull(message: ProtoWireFormatDecoder?): Long? =
        message?.longOrNull(1, "")

    override fun decodeLongList(message: ProtoWireFormatDecoder?): List<Long> =
        message?.longList(1, "") ?: emptyList()

    override fun decodeLongListOrNull(message: ProtoWireFormatDecoder?): List<Long>? =
        message?.longListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    override fun encodeFloat(value: Float?): ByteArray =
        ProtoWireFormatEncoder().floatOrNull(1, "", value).pack()

    override fun encodeFloatList(value: List<Float>?): ByteArray =
        ProtoWireFormatEncoder().floatListOrNull(1, "", value).pack()

    override fun decodeFloat(message: ProtoWireFormatDecoder?): Float =
        message?.float(1, "") ?: 0f

    override fun decodeFloatOrNull(message: ProtoWireFormatDecoder?): Float? =
        message?.floatOrNull(1, "")

    override fun decodeFloatList(message: ProtoWireFormatDecoder?): List<Float> =
        message?.floatList(1, "") ?: emptyList()

    override fun decodeFloatListOrNull(message: ProtoWireFormatDecoder?): List<Float>? =
        message?.floatListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    override fun encodeDouble(value: Double?): ByteArray =
        ProtoWireFormatEncoder().doubleOrNull(1, "", value).pack()

    override fun encodeDoubleList(value: List<Double>?): ByteArray =
        ProtoWireFormatEncoder().doubleListOrNull(1, "", value).pack()

    override fun decodeDouble(message: ProtoWireFormatDecoder?): Double =
        message?.double(1, "") ?: 0.0

    override fun decodeDoubleOrNull(message: ProtoWireFormatDecoder?): Double? =
        message?.doubleOrNull(1, "")

    override fun decodeDoubleList(message: ProtoWireFormatDecoder?): List<Double> =
        message?.doubleList(1, "") ?: emptyList()

    override fun decodeDoubleListOrNull(message: ProtoWireFormatDecoder?): List<Double>? =
        message?.doubleListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    override fun encodeChar(value: Char?): ByteArray =
        ProtoWireFormatEncoder().charOrNull(1, "", value).pack()

    override fun encodeCharList(value: List<Char>?): ByteArray =
        ProtoWireFormatEncoder().charListOrNull(1, "", value).pack()

    override fun decodeChar(message: ProtoWireFormatDecoder?): Char =
        message?.char(1, "") ?: Char.MIN_VALUE

    override fun decodeCharOrNull(message: ProtoWireFormatDecoder?): Char? =
        message?.charOrNull(1, "")

    override fun decodeCharList(message: ProtoWireFormatDecoder?): List<Char> =
        message?.charList(1, "") ?: emptyList()

    override fun decodeCharListOrNull(message: ProtoWireFormatDecoder?): List<Char>? =
        message?.charListOrNull(1, "")
    
    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    override fun encodeString(value: String?): ByteArray =
        ProtoWireFormatEncoder().stringOrNull(1, "", value).pack()

    override fun encodeStringList(value: List<String>?): ByteArray =
        ProtoWireFormatEncoder().stringListOrNull(1, "", value).pack()

    override fun decodeString(message: ProtoWireFormatDecoder?): String =
        message?.string(1, "") ?: ""

    override fun decodeStringOrNull(message: ProtoWireFormatDecoder?): String? =
        message?.stringOrNull(1, "")

    override fun decodeStringList(message: ProtoWireFormatDecoder?): List<String> =
        message?.stringList(1, "") ?: emptyList()

    override fun decodeStringListOrNull(message: ProtoWireFormatDecoder?): List<String>? =
        message?.stringListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    override fun encodeByteArray(value: ByteArray?): ByteArray =
        ProtoWireFormatEncoder().byteArrayOrNull(1, "", value).pack()

    override fun encodeByteArrayList(value: List<ByteArray>?): ByteArray =
        ProtoWireFormatEncoder().byteArrayListOrNull(1, "", value).pack()

    override fun decodeByteArray(message: ProtoWireFormatDecoder?): ByteArray =
        message?.byteArray(1, "") ?: ByteArray(0)

    override fun decodeByteArrayOrNull(message: ProtoWireFormatDecoder?): ByteArray? =
        message?.byteArrayOrNull(1, "")

    override fun decodeByteArrayList(message: ProtoWireFormatDecoder?): List<ByteArray> =
        message?.byteArrayList(1, "") ?: emptyList()

    override fun decodeByteArrayListOrNull(message: ProtoWireFormatDecoder?): List<ByteArray>? =
        message?.byteArrayListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    override fun encodeUuid(value: UUID<*>?): ByteArray =
        ProtoWireFormatEncoder().uuidOrNull(1, "", value).pack()

    override fun encodeUuidList(value: List<UUID<*>>?): ByteArray =
        ProtoWireFormatEncoder().uuidListOrNull(1, "", value).pack()

    override fun decodeUuid(message: ProtoWireFormatDecoder?): UUID<Any> =
        message?.uuid(1, "") ?: UUID()

    override fun decodeUuidOrNull(message: ProtoWireFormatDecoder?): UUID<Any>? =
        message?.uuidOrNull(1, "")

    override fun decodeUuidList(message: ProtoWireFormatDecoder?): List<UUID<Any>> =
        message?.uuidList(1, "") ?: emptyList()

    override fun decodeUuidListOrNull(message: ProtoWireFormatDecoder?): List<UUID<Any>>? =
        message?.uuidListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    override fun <T> encodeInstance(value: T?, wireFormat: WireFormat<T>): ByteArray =
        ProtoWireFormatEncoder().instanceOrNull(1, "", value, wireFormat).pack()

    override fun <T> encodeInstanceList(value: List<T>?, wireFormat: WireFormat<T>): ByteArray =
        ProtoWireFormatEncoder().instanceListOrNull(1, "", value, wireFormat).pack()

    override fun <T> decodeInstance(message: ProtoWireFormatDecoder?, decoder: WireFormat<T>): T =
        checkNotNull(message?.instance(1, "", decoder)) { "cannot decode instance with $decoder" }

    override fun <T> decodeInstanceOrNull(message: ProtoWireFormatDecoder?, decoder: WireFormat<T>): T? =
        message?.instanceOrNull(1, "", decoder)

    override fun <T> decodeInstanceList(message: ProtoWireFormatDecoder?, decoder: WireFormat<T>): List<T> =
        message?.instanceList(1, "", decoder) ?: emptyList()

    override fun <T> decodeInstanceListOrNull(message: ProtoWireFormatDecoder?, decoder: WireFormat<T>): List<T>? =
        message?.instanceListOrNull(1, "", decoder)

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    override fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray =
        ProtoWireFormatEncoder().enumOrNull(1, "", value, entries).pack()

    override fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray =
        ProtoWireFormatEncoder().enumListOrNull(1, "", value, entries).pack()

    override fun <E : Enum<E>> decodeEnum(message: ProtoWireFormatDecoder?, entries: EnumEntries<E>): E {
        if (message == null) return entries.first()
        return entries[message.int(1, "")]
    }

    override fun <E : Enum<E>> decodeEnumOrNull(message: ProtoWireFormatDecoder?, entries: EnumEntries<E>): E? {
        if (message == null) return null
        return message.intOrNull(1, "")?.let { entries[it] }
    }

    override fun <E : Enum<E>> decodeEnumList(message: ProtoWireFormatDecoder?, entries: EnumEntries<E>): List<E> {
        if (message == null) return emptyList()
        return message.intList(1, "").map { entries[it] }
    }

    override fun <E : Enum<E>> decodeEnumListOrNull(message: ProtoWireFormatDecoder?, entries: EnumEntries<E>): List<E>? {
        if (message == null) return null
        return message.intListOrNull(1, "")?.map { entries[it] }
    }

    // ---------------------------------------------------------------------------
    // UInt
    // ---------------------------------------------------------------------------

    override fun encodeUInt(value: UInt?): ByteArray =
        ProtoWireFormatEncoder().uIntOrNull(1, "", value).pack()

    override fun encodeUIntList(value: List<UInt>?): ByteArray =
        ProtoWireFormatEncoder().uIntListOrNull(1, "", value).pack()

    override fun decodeUInt(message: ProtoWireFormatDecoder?): UInt =
        message?.uInt(1, "") ?: UInt.MIN_VALUE

    override fun decodeUIntOrNull(message: ProtoWireFormatDecoder?): UInt? =
        message?.uIntOrNull(1, "")

    override fun decodeUIntList(message: ProtoWireFormatDecoder?): List<UInt> =
        message?.uIntList(1, "") ?: emptyList()

    override fun decodeUIntListOrNull(message: ProtoWireFormatDecoder?): List<UInt>? =
        message?.uIntListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    override fun encodeUShort(value: UShort?): ByteArray =
        ProtoWireFormatEncoder().uShortOrNull(1, "", value).pack()

    override fun encodeUShortList(value: List<UShort>?): ByteArray =
        ProtoWireFormatEncoder().uShortListOrNull(1, "", value).pack()

    override fun decodeUShort(message: ProtoWireFormatDecoder?): UShort =
        message?.uShort(1, "") ?: UShort.MIN_VALUE

    override fun decodeUShortOrNull(message: ProtoWireFormatDecoder?): UShort? =
        message?.uShortOrNull(1, "")

    override fun decodeUShortList(message: ProtoWireFormatDecoder?): List<UShort> =
        message?.uShortList(1, "") ?: emptyList()

    override fun decodeUShortListOrNull(message: ProtoWireFormatDecoder?): List<UShort>? =
        message?.uShortListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    override fun encodeUByte(value: UByte?): ByteArray =
        ProtoWireFormatEncoder().uByteOrNull(1, "", value).pack()

    override fun encodeUByteList(value: List<UByte>?): ByteArray =
        ProtoWireFormatEncoder().uByteListOrNull(1, "", value).pack()

    override fun decodeUByte(message: ProtoWireFormatDecoder?): UByte =
        message?.uByte(1, "") ?: UByte.MIN_VALUE

    override fun decodeUByteOrNull(message: ProtoWireFormatDecoder?): UByte? =
        message?.uByteOrNull(1, "")

    override fun decodeUByteList(message: ProtoWireFormatDecoder?): List<UByte> =
        message?.uByteList(1, "") ?: emptyList()

    override fun decodeUByteListOrNull(message: ProtoWireFormatDecoder?): List<UByte>? =
        message?.uByteListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    override fun encodeULong(value: ULong?): ByteArray =
        ProtoWireFormatEncoder().uLongOrNull(1, "", value).pack()

    override fun encodeULongList(value: List<ULong>?): ByteArray =
        ProtoWireFormatEncoder().uLongListOrNull(1, "", value).pack()

    override fun decodeULong(message: ProtoWireFormatDecoder?): ULong =
        message?.uLong(1, "") ?: ULong.MIN_VALUE

    override fun decodeULongOrNull(message: ProtoWireFormatDecoder?): ULong? =
        message?.uLongOrNull(1, "")

    override fun decodeULongList(message: ProtoWireFormatDecoder?): List<ULong> =
        message?.uLongList(1, "") ?: emptyList()

    override fun decodeULongListOrNull(message: ProtoWireFormatDecoder?): List<ULong>? =
        message?.uLongListOrNull(1, "")

}
