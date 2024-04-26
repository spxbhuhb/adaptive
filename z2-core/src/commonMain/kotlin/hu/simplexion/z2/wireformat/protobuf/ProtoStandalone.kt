package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormat
import kotlin.enums.EnumEntries

object ProtoStandalone : Standalone {

    fun encoder(): ProtoWireFormatEncoder =
        ProtoWireFormatEncoder()

    fun decoder(byteArray: ByteArray): ProtoWireFormatDecoder =
        ProtoWireFormatDecoder(byteArray)
    
    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    override fun encodeAny(value: Any?): ByteArray =
        TODO()

    override fun encodeAnyList(value: List<Any>?): ByteArray =
        TODO()

    override fun decodeAny(source: ByteArray): Any =
        TODO()

    override fun decodeAnyOrNull(source: ByteArray): Any =
        TODO()

    override fun decodeAnyList(source: ByteArray): List<Any> =
        TODO()

    override fun decodeAnyListOrNull(source: ByteArray): List<Any> =
        TODO()

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    override fun encodeUnit(value: Unit?): ByteArray =
        encoder().unitOrNull(1, "", value).pack()

    override fun encodeUnitList(value: List<Unit>?): ByteArray =
        encoder().unitListOrNull(1, "", value).pack()

    override fun decodeUnit(source: ByteArray): Unit =
        checkNotNull(decoder(source).unit(1, ""))

    override fun decodeUnitOrNull(source: ByteArray): Unit? =
        decoder(source).unitOrNull(1, "")

    override fun decodeUnitList(source: ByteArray): List<Unit> =
        decoder(source).unitList(1, "")

    override fun decodeUnitListOrNull(source: ByteArray): List<Unit>? =
        decoder(source).unitListOrNull(1, "")
    
    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    override fun encodeBoolean(value: Boolean?): ByteArray =
        encoder().booleanOrNull(1, "", value).pack()

    override fun encodeBooleanList(value: List<Boolean>?): ByteArray =
        encoder().booleanArrayOrNull(1, "", value).pack()

    override fun decodeBoolean(source: ByteArray): Boolean =
        decoder(source).boolean(1, "")

    override fun decodeBooleanOrNull(source: ByteArray): Boolean? =
        decoder(source).booleanOrNull(1, "")

    override fun decodeBooleanList(source: ByteArray): List<Boolean> =
        decoder(source).booleanArray(1, "")

    override fun decodeBooleanListOrNull(source: ByteArray): List<Boolean>? =
        decoder(source).booleanArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    override fun encodeInt(value: Int?): ByteArray =
        encoder().intOrNull(1, "", value).pack()

    override fun encodeIntList(value: List<Int>?): ByteArray =
        encoder().intArrayOrNull(1, "", value).pack()

    override fun decodeInt(source: ByteArray): Int =
        decoder(source).int(1, "")

    override fun decodeIntOrNull(source: ByteArray): Int? =
        decoder(source).intOrNull(1, "")

    override fun decodeIntList(source: ByteArray): List<Int> =
        decoder(source).intArray(1, "")

    override fun decodeIntListOrNull(source: ByteArray): List<Int>? =
        decoder(source).intArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Short
    // ---------------------------------------------------------------------------

    override fun encodeShort(value: Short?): ByteArray =
        encoder().shortOrNull(1, "", value).pack()

    override fun encodeShortList(value: List<Short>?): ByteArray =
        encoder().shortArrayOrNull(1, "", value).pack()

    override fun decodeShort(source: ByteArray): Short =
        decoder(source).short(1, "")

    override fun decodeShortOrNull(source: ByteArray): Short? =
        decoder(source).shortOrNull(1, "")

    override fun decodeShortList(source: ByteArray): List<Short> =
        decoder(source).shortArray(1, "")

    override fun decodeShortListOrNull(source: ByteArray): List<Short>? =
        decoder(source).shortArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    override fun encodeByte(value: Byte?): ByteArray =
        encoder().byteOrNull(1, "", value).pack()

    override fun encodeByteList(value: List<Byte>?): ByteArray =
        encoder().byteListOrNull(1, "", value).pack()

    override fun decodeByte(source: ByteArray): Byte =
        decoder(source).byte(1, "")

    override fun decodeByteOrNull(source: ByteArray): Byte? =
        decoder(source).byteOrNull(1, "")

    override fun decodeByteList(source: ByteArray): List<Byte> =
        decoder(source).byteArray(1, "")

    override fun decodeByteListOrNull(source: ByteArray): List<Byte>? =
        decoder(source).byteListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    override fun encodeLong(value: Long?): ByteArray =
        encoder().longOrNull(1, "", value).pack()

    override fun encodeLongList(value: List<Long>?): ByteArray =
        encoder().longArrayOrNull(1, "", value).pack()

    override fun decodeLong(source: ByteArray): Long =
        decoder(source).long(1, "")

    override fun decodeLongOrNull(source: ByteArray): Long? =
        decoder(source).longOrNull(1, "")

    override fun decodeLongList(source: ByteArray): List<Long> =
        decoder(source).longArray(1, "")

    override fun decodeLongListOrNull(source: ByteArray): List<Long>? =
        decoder(source).longArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    override fun encodeFloat(value: Float?): ByteArray =
        encoder().floatOrNull(1, "", value).pack()

    override fun encodeFloatList(value: List<Float>?): ByteArray =
        encoder().floatArrayOrNull(1, "", value).pack()

    override fun decodeFloat(source: ByteArray): Float =
        decoder(source).float(1, "")

    override fun decodeFloatOrNull(source: ByteArray): Float? =
        decoder(source).floatOrNull(1, "")

    override fun decodeFloatList(source: ByteArray): List<Float> =
        decoder(source).floatArray(1, "")

    override fun decodeFloatListOrNull(source: ByteArray): List<Float>? =
        decoder(source).floatArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    override fun encodeDouble(value: Double?): ByteArray =
        encoder().doubleOrNull(1, "", value).pack()

    override fun encodeDoubleList(value: List<Double>?): ByteArray =
        encoder().doubleArrayOrNull(1, "", value).pack()

    override fun decodeDouble(source: ByteArray): Double =
        decoder(source).double(1, "")

    override fun decodeDoubleOrNull(source: ByteArray): Double? =
        decoder(source).doubleOrNull(1, "")

    override fun decodeDoubleList(source: ByteArray): List<Double> =
        decoder(source).doubleArray(1, "")

    override fun decodeDoubleListOrNull(source: ByteArray): List<Double>? =
        decoder(source).doubleArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    override fun encodeChar(value: Char?): ByteArray =
        encoder().charOrNull(1, "", value).pack()

    override fun encodeCharList(value: List<Char>?): ByteArray =
        encoder().charArrayOrNull(1, "", value).pack()

    override fun decodeChar(source: ByteArray): Char =
        decoder(source).char(1, "")

    override fun decodeCharOrNull(source: ByteArray): Char? =
        decoder(source).charOrNull(1, "")

    override fun decodeCharList(source: ByteArray): List<Char> =
        decoder(source).charArray(1, "")

    override fun decodeCharListOrNull(source: ByteArray): List<Char>? =
        decoder(source).charArrayOrNull(1, "")
    
    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    override fun encodeString(value: String?): ByteArray =
        encoder().stringOrNull(1, "", value).pack()

    override fun encodeStringList(value: List<String>?): ByteArray =
        encoder().stringListOrNull(1, "", value).pack()

    override fun decodeString(source: ByteArray): String =
        decoder(source).string(1, "")

    override fun decodeStringOrNull(source: ByteArray): String? =
        decoder(source).stringOrNull(1, "")

    override fun decodeStringList(source: ByteArray): List<String> =
        decoder(source).stringList(1, "")

    override fun decodeStringListOrNull(source: ByteArray): List<String>? =
        decoder(source).stringListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    override fun encodeByteArray(value: ByteArray?): ByteArray =
        encoder().byteArrayOrNull(1, "", value).pack()

    override fun encodeByteArrayList(value: List<ByteArray>?): ByteArray =
        encoder().byteArrayListOrNull(1, "", value).pack()

    override fun decodeByteArray(source: ByteArray): ByteArray =
        decoder(source).byteArray(1, "")

    override fun decodeByteArrayOrNull(source: ByteArray): ByteArray? =
        decoder(source).byteArrayOrNull(1, "")

    override fun decodeByteArrayList(source: ByteArray): List<ByteArray> =
        decoder(source).byteArrayList(1, "")

    override fun decodeByteArrayListOrNull(source: ByteArray): List<ByteArray>? =
        decoder(source).byteArrayListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    override fun encodeUuid(value: UUID<*>?): ByteArray =
        encoder().uuidOrNull(1, "", value).pack()

    override fun encodeUuidList(value: List<UUID<*>>?): ByteArray =
        encoder().uuidListOrNull(1, "", value).pack()

    override fun decodeUuid(source: ByteArray): UUID<Any> =
        decoder(source).uuid(1, "")

    override fun decodeUuidOrNull(source: ByteArray): UUID<Any>? =
        decoder(source).uuidOrNull(1, "")

    override fun decodeUuidList(source: ByteArray): List<UUID<Any>> =
        decoder(source).uuidList(1, "")

    override fun decodeUuidListOrNull(source: ByteArray): List<UUID<Any>>? =
        decoder(source).uuidListOrNull(1, "")

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    override fun <T> encodeInstance(value: T?, wireFormat: WireFormat<T>): ByteArray =
        encoder().instanceOrNull(1, "", value, wireFormat).pack()

    override fun <T> encodeInstanceList(value: List<T>?, wireFormat: WireFormat<T>): ByteArray =
        encoder().instanceListOrNull(1, "", value, wireFormat).pack()

    override fun <T> decodeInstance(source: ByteArray, wireFormat: WireFormat<T>): T =
        checkNotNull(decoder(source).instance(1, "", wireFormat)) { "cannot decode instance with $wireFormat" }

    override fun <T> decodeInstanceOrNull(source: ByteArray, wireFormat: WireFormat<T>): T? =
        decoder(source).instanceOrNull(1, "", wireFormat)

    override fun <T> decodeInstanceList(source: ByteArray, wireFormat: WireFormat<T>): List<T> =
        decoder(source).collection(1, "", wireFormat)

    override fun <T> decodeInstanceListOrNull(source: ByteArray, wireFormat: WireFormat<T>): List<T>? =
        decoder(source).collectionOrNull(1, "", wireFormat)

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    override fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray =
        encoder().enumOrNull(1, "", value, entries).pack()

    override fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray =
        encoder().enumListOrNull(1, "", value, entries).pack()

    override fun <E : Enum<E>> decodeEnum(source: ByteArray, entries: EnumEntries<E>): E =
        entries[decoder(source).int(1, "")]

    override fun <E : Enum<E>> decodeEnumOrNull(source: ByteArray, entries: EnumEntries<E>): E? =
        decoder(source).intOrNull(1, "")?.let { entries[it] }

    override fun <E : Enum<E>> decodeEnumList(source: ByteArray, entries: EnumEntries<E>): List<E> =
        decoder(source).intArray(1, "").map { entries[it] }

    override fun <E : Enum<E>> decodeEnumListOrNull(source: ByteArray, entries: EnumEntries<E>): List<E>? =
        decoder(source).intArrayOrNull(1, "")?.map { entries[it] }

    // ---------------------------------------------------------------------------
    // UInt
    // ---------------------------------------------------------------------------

    override fun encodeUInt(value: UInt?): ByteArray =
        encoder().uIntOrNull(1, "", value).pack()

    override fun encodeUIntList(value: List<UInt>?): ByteArray =
        encoder().uIntArrayOrNull(1, "", value).pack()

    override fun decodeUInt(source: ByteArray): UInt =
        decoder(source).uInt(1, "")

    override fun decodeUIntOrNull(source: ByteArray): UInt? =
        decoder(source).uIntOrNull(1, "")

    override fun decodeUIntList(source: ByteArray): List<UInt> =
        decoder(source).uIntArray(1, "")

    override fun decodeUIntListOrNull(source: ByteArray): List<UInt>? =
        decoder(source).uIntArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    override fun encodeUShort(value: UShort?): ByteArray =
        encoder().uShortOrNull(1, "", value).pack()

    override fun encodeUShortList(value: List<UShort>?): ByteArray =
        encoder().uShortArrayOrNull(1, "", value).pack()

    override fun decodeUShort(source: ByteArray): UShort =
        decoder(source).uShort(1, "")

    override fun decodeUShortOrNull(source: ByteArray): UShort? =
        decoder(source).uShortOrNull(1, "")

    override fun decodeUShortList(source: ByteArray): List<UShort> =
        decoder(source).uShortArray(1, "")

    override fun decodeUShortListOrNull(source: ByteArray): List<UShort>? =
        decoder(source).uShortArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    override fun encodeUByte(value: UByte?): ByteArray =
        encoder().uByteOrNull(1, "", value).pack()

    override fun encodeUByteList(value: List<UByte>?): ByteArray =
        encoder().uByteArrayOrNull(1, "", value).pack()

    override fun decodeUByte(source: ByteArray): UByte =
        decoder(source).uByte(1, "")

    override fun decodeUByteOrNull(source: ByteArray): UByte? =
        decoder(source).uByteOrNull(1, "")

    override fun decodeUByteList(source: ByteArray): List<UByte> =
        decoder(source).uByteArray(1, "")

    override fun decodeUByteListOrNull(source: ByteArray): List<UByte>? =
        decoder(source).uByteArrayOrNull(1, "")

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    override fun encodeULong(value: ULong?): ByteArray =
        encoder().uLongOrNull(1, "", value).pack()

    override fun encodeULongList(value: List<ULong>?): ByteArray =
        encoder().uLongArrayOrNull(1, "", value).pack()

    override fun decodeULong(source: ByteArray): ULong =
        decoder(source).uLong(1, "")

    override fun decodeULongOrNull(source: ByteArray): ULong? =
        decoder(source).uLongOrNull(1, "")

    override fun decodeULongList(source: ByteArray): List<ULong> =
        decoder(source).uLongArray(1, "")

    override fun decodeULongListOrNull(source: ByteArray): List<ULong>? =
        decoder(source).uLongArrayOrNull(1, "")

}
