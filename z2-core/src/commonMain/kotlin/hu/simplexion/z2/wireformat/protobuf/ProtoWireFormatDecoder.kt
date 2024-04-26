package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder
import kotlin.enums.EnumEntries

/**
 * Parse Protocol Buffer messages.
 *
 * @param  wireFormat  The wire format message to parse. This buffer backs the parser, it should
 *                     not change until the message is in use.
 */
class ProtoWireFormatDecoder(
    wireFormat: ByteArray,
    offset: Int = 0,
    length: Int = wireFormat.size
) : WireFormatDecoder<ProtoRecord> {

    // TODO proto record performance for big collections, a structured storage would be better
    val records: List<ProtoRecord> = ProtoBufferReader(wireFormat, offset, length).records()

    operator fun get(fieldNumber: Int): ProtoRecord? = records.lastOrNull { it.fieldNumber == fieldNumber }

    // -----------------------------------------------------------------------------------------
    // Any
    // -----------------------------------------------------------------------------------------

    override fun any(fieldNumber: Int, fieldName: String): Any =
        TODO()

    override fun anyOrNull(fieldNumber: Int, fieldName: String): Any =
        TODO()

    override fun rawAny(source: ProtoRecord): Any {
        TODO("Not yet implemented")
    }

    // -----------------------------------------------------------------------------------------
    // Unit
    // -----------------------------------------------------------------------------------------

    override fun unit(fieldNumber: Int, fieldName: String) {
        check(get(fieldNumber)?.value == 1UL)
    }

    override fun unitOrNull(fieldNumber: Int, fieldName: String): Unit? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else unit(fieldNumber, fieldName)

    override fun rawUnit(source: ProtoRecord) {
        check(source.value == 1UL)
    }

    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String): Boolean =
        get(fieldNumber)?.let { it.value == 1UL } ?: false

    override fun booleanOrNull(fieldNumber: Int, fieldName: String): Boolean? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else boolean(fieldNumber, fieldName)

    override fun booleanArray(fieldNumber: Int, fieldName: String): BooleanArray =
        scalarList(fieldNumber, { value.bool() }, { varint().bool() })

    override fun booleanArrayOrNull(fieldNumber: Int, fieldName: String): BooleanArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else booleanArray(fieldNumber, fieldName)

    override fun rawBoolean(source: ProtoRecord): Boolean {
        return source.value == 1UL
    }

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String): Int =
        get(fieldNumber)?.value?.sint32() ?: 0

    override fun intOrNull(fieldNumber: Int, fieldName: String): Int? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else int(fieldNumber, fieldName)

    override fun intArray(fieldNumber: Int, fieldName: String): IntArray =
        scalarList(fieldNumber, { value.sint32() }, { varint().sint32() })

    override fun intArrayOrNull(fieldNumber: Int, fieldName: String): IntArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else intArray(fieldNumber, fieldName)

    override fun rawInt(source: ProtoRecord): Int {
        return source.value.sint32()
    }

    // -----------------------------------------------------------------------------------------
    // Short
    // -----------------------------------------------------------------------------------------

    override fun short(fieldNumber: Int, fieldName: String): Short =
        get(fieldNumber)?.value?.sint32()?.toShort() ?: 0

    override fun shortOrNull(fieldNumber: Int, fieldName: String): Short? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else short(fieldNumber, fieldName)

    override fun shortArray(fieldNumber: Int, fieldName: String): ShortArray =
        scalarList(fieldNumber, { value.sint32().toShort() }, { varint().sint32().toShort() })

    override fun shortArrayOrNull(fieldNumber: Int, fieldName: String): ShortArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else shortArray(fieldNumber, fieldName)

    override fun rawShort(source: ProtoRecord): Short {
        return source.value.sint32().toShort()
    }

    // -----------------------------------------------------------------------------------------
    // Byte
    // -----------------------------------------------------------------------------------------

    override fun byte(fieldNumber: Int, fieldName: String): Byte =
        get(fieldNumber)?.value?.sint32()?.toByte() ?: 0

    override fun byteOrNull(fieldNumber: Int, fieldName: String): Byte? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byte(fieldNumber, fieldName)

    override fun byteArray(fieldNumber: Int, fieldName: String): ByteArray =
        scalarList(fieldNumber, { value.sint32().toByte() }, { varint().sint32().toByte() })

    override fun byteListOrNull(fieldNumber: Int, fieldName: String): List<Byte>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byteArray(fieldNumber, fieldName)

    override fun rawByte(source: ProtoRecord): Byte {
        return source.value.sint32().toByte()
    }

    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String): Long =
        get(fieldNumber)?.value?.sint64() ?: 0L

    override fun longOrNull(fieldNumber: Int, fieldName: String): Long? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else long(fieldNumber, fieldName)

    override fun longArray(fieldNumber: Int, fieldName: String): LongArray =
        scalarList(fieldNumber, { value.sint64() }, { varint().sint64() })

    override fun longArrayOrNull(fieldNumber: Int, fieldName: String): LongArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else longArray(fieldNumber, fieldName)

    override fun rawLong(source: ProtoRecord): Long {
        return source.value.sint64()
    }

    // -----------------------------------------------------------------------------------------
    // Float
    // -----------------------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String): Float =
        get(fieldNumber)?.value?.float() ?: 0f

    override fun floatOrNull(fieldNumber: Int, fieldName: String): Float? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else float(fieldNumber, fieldName)

    override fun floatArray(fieldNumber: Int, fieldName: String): Float =
        scalarList(fieldNumber, { value.float() }, { i32().float() })

    override fun floatArrayOrNull(fieldNumber: Int, fieldName: String): FloatArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else floatArray(fieldNumber, fieldName)

    override fun rawFloat(source: ProtoRecord): Float {
        return source.value.float()
    }
    // -----------------------------------------------------------------------------------------
    // Double
    // -----------------------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String): Double =
        get(fieldNumber)?.value?.double() ?: 0.0

    override fun doubleOrNull(fieldNumber: Int, fieldName: String): Double? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else double(fieldNumber, fieldName)

    override fun doubleArray(fieldNumber: Int, fieldName: String): DoubleArray =
        scalarList(fieldNumber, { value.double() }, { i64().double() })

    override fun doubleArrayOrNull(fieldNumber: Int, fieldName: String): DoubleArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else doubleArray(fieldNumber, fieldName)

    override fun rawDouble(source: ProtoRecord): Double {
        return source.value.double()
    }

    // -----------------------------------------------------------------------------------------
    // Char
    // -----------------------------------------------------------------------------------------

    override fun char(fieldNumber: Int, fieldName: String): Char =
        get(fieldNumber)?.value?.sint32()?.toChar() ?: Char.MIN_VALUE

    override fun charOrNull(fieldNumber: Int, fieldName: String): Char? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else char(fieldNumber, fieldName)

    override fun charArray(fieldNumber: Int, fieldName: String): CharArray =
        scalarList(fieldNumber, { value.sint32().toChar() }, { varint().sint32().toChar() })

    override fun charArrayOrNull(fieldNumber: Int, fieldName: String): CharArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else charArray(fieldNumber, fieldName)

    override fun rawChar(source: ProtoRecord): Char {
        return source.value.sint32().toChar()
    }

    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String): String =
        get(fieldNumber)?.string() ?: ""

    override fun stringOrNull(fieldNumber: Int, fieldName: String): String? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else string(fieldNumber, fieldName)

    override fun stringList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { string() }, { string() })

    override fun stringListOrNull(fieldNumber: Int, fieldName: String): List<String>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else stringList(fieldNumber, fieldName)

    override fun rawString(source: ProtoRecord): String {
        return source.string()
    }

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String): ByteArray =
        get(fieldNumber)?.bytes() ?: ByteArray(0)

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byteArray(fieldNumber, fieldName)

    override fun byteArrayList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { bytes() }, { bytes() })

    override fun byteArrayListOrNull(fieldNumber: Int, fieldName: String): List<ByteArray>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byteArrayList(fieldNumber, fieldName)

    override fun rawByteArray(source: ProtoRecord): ByteArray {
        return source.bytes()
    }

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    override fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T> =
        get(fieldNumber)?.uuid() ?: UUID.nil()

    override fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uuid(fieldNumber, fieldName)

    override fun <T> uuidList(fieldNumber: Int, fieldName: String): List<UUID<T>> =
        scalarList(fieldNumber, { uuid() }, { uuid() })

    override fun <T> uuidListOrNull(fieldNumber: Int, fieldName: String): List<UUID<T>>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uuidList(fieldNumber, fieldName)

    override fun rawUuid(source: ProtoRecord): UUID<*> {
        return source.uuid<Any>()
    }

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T {
        val record = get(fieldNumber) ?: return wireFormat.wireFormatDecode(null, null)
        check(record is LenProtoRecord)
        return wireFormat.wireFormatDecode(record, record.decoder())
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else instance(fieldNumber, fieldName, wireFormat)

    override fun <T> collection(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): Collection<T> {
        val list = mutableListOf<T>()
        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue
            check(record is LenProtoRecord)
            list += wireFormat.wireFormatDecode(record, record.decoder())
        }
        return list
    }

    override fun <T> collectionOrNull(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): Collection<T>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else collection(fieldNumber, fieldName, wireFormat)

    override fun <T> rawInstance(source: ProtoRecord, wireFormat: WireFormat<T>): T {
        check(source is LenProtoRecord)
        return wireFormat.wireFormatDecode(source, source.decoder())
    }

    // -----------------------------------------------------------------------------------------
    // Enum
    // -----------------------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E =
        entries[checkNotNull(get(fieldNumber)).value.toInt()]

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E? =
        get(fieldNumber)?.let { entries[it.value.toInt()] }

    override fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): MutableList<E> =
        intArray(fieldNumber, fieldName).map { entries[it] }.toMutableList()

    override fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): List<E>? =
        intArrayOrNull(fieldNumber, fieldName)?.map { entries[it] }

    override fun <E : Enum<E>> rawEnum(source: ProtoRecord, entries: EnumEntries<E>): E {
        return entries[source.value.toInt()]
    }

    // -----------------------------------------------------------------------------------------
    // UInt
    // -----------------------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String): UInt =
        get(fieldNumber)?.value?.toUInt() ?: UInt.MIN_VALUE

    override fun uIntOrNull(fieldNumber: Int, fieldName: String): UInt? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uInt(fieldNumber, fieldName)

    override fun uIntArray(fieldNumber: Int, fieldName: String): UIntArray =
        scalarList(fieldNumber, { value.toUInt() }, { i32().toUInt() })

    override fun uIntArrayOrNull(fieldNumber: Int, fieldName: String): UIntArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uIntArray(fieldNumber, fieldName)

    override fun rawUInt(source: ProtoRecord): UInt {
        return source.value.toUInt()
    }

    // -----------------------------------------------------------------------------------------
    // UShort
    // -----------------------------------------------------------------------------------------

    override fun uShort(fieldNumber: Int, fieldName: String): UShort =
        get(fieldNumber)?.value?.sint32()?.toUShort() ?: UShort.MIN_VALUE

    override fun uShortOrNull(fieldNumber: Int, fieldName: String): UShort? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uShort(fieldNumber, fieldName)

    override fun uShortArray(fieldNumber: Int, fieldName: String): UShortArray =
        scalarList(fieldNumber, { value.sint32().toUShort() }, { varint().sint32().toUShort() })

    override fun uShortArrayOrNull(fieldNumber: Int, fieldName: String): UShortArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uShortArray(fieldNumber, fieldName)

    override fun rawUShort(source: ProtoRecord): UShort {
        return source.value.sint32().toUShort()
    }

    // -----------------------------------------------------------------------------------------
    // UByte
    // -----------------------------------------------------------------------------------------

    override fun uByte(fieldNumber: Int, fieldName: String): UByte =
        get(fieldNumber)?.value?.sint32()?.toUByte() ?: UByte.MIN_VALUE

    override fun uByteOrNull(fieldNumber: Int, fieldName: String): UByte? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uByte(fieldNumber, fieldName)

    override fun uByteArray(fieldNumber: Int, fieldName: String): UByteArray =
        scalarList(fieldNumber, { value.sint32().toUByte() }, { varint().sint32().toUByte() })

    override fun uByteArrayOrNull(fieldNumber: Int, fieldName: String): UByteArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uByteArray(fieldNumber, fieldName)

    override fun rawUByte(source: ProtoRecord): UByte {
        return source.value.sint32().toUByte()
    }

    // -----------------------------------------------------------------------------------------
    // ULong
    // -----------------------------------------------------------------------------------------

    override fun uLong(fieldNumber: Int, fieldName: String): ULong =
        get(fieldNumber)?.value ?: ULong.MIN_VALUE

    override fun uLongOrNull(fieldNumber: Int, fieldName: String): ULong? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uLong(fieldNumber, fieldName)

    override fun uLongArray(fieldNumber: Int, fieldName: String): ULongArray =
        scalarList(fieldNumber, { value }, { i64() })

    override fun uLongArrayOrNull(fieldNumber: Int, fieldName: String): ULongArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uLongArray(fieldNumber, fieldName)

    override fun rawULong(source: ProtoRecord): ULong {
        return source.value
    }

    // --------------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------------

    fun <T> scalarList(
        fieldNumber: Int,
        single: ProtoRecord.() -> T,
        item: ProtoBufferReader.() -> T
    ): MutableList<T> {
        val list = mutableListOf<T>()

        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue

            if (record !is LenProtoRecord) {
                list += record.single()
            } else {
                list += ProtoBufferReader(record).packed(item)
            }
        }
        return list
    }

}