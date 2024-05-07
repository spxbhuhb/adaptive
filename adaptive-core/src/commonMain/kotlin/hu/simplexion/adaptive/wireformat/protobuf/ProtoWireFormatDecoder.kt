/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.protobuf

import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatKind
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument
import kotlin.enums.EnumEntries

/**
 * Parse Protocol Buffer messages.
 *
 * @param  wireFormat  The wire format message to parse. This buffer backs the parser, it should
 *                     not change until the message is in use.
 */
@OptIn(ExperimentalUnsignedTypes::class)
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

    override fun rawChar(source: ProtoRecord): Char {
        return source.value.sint32().toChar()
    }

    // -----------------------------------------------------------------------------------------
    // BooleanArray
    // -----------------------------------------------------------------------------------------

    override fun booleanArray(fieldNumber: Int, fieldName: String): BooleanArray =
        scalarList(fieldNumber, { value.bool() }, { varint().bool() }).toBooleanArray()

    override fun booleanArrayOrNull(fieldNumber: Int, fieldName: String): BooleanArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else booleanArray(fieldNumber, fieldName)

    override fun rawBooleanArray(source: ProtoRecord): BooleanArray =
        ProtoBufferReader(source as LenProtoRecord).packed { varint().bool() }.toBooleanArray()

    // -----------------------------------------------------------------------------------------
    // IntArray
    // -----------------------------------------------------------------------------------------

    override fun intArray(fieldNumber: Int, fieldName: String): IntArray =
        scalarList(fieldNumber, { value.sint32() }, { varint().sint32() }).toIntArray()

    override fun intArrayOrNull(fieldNumber: Int, fieldName: String): IntArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else intArray(fieldNumber, fieldName)

    override fun rawIntArray(source: ProtoRecord): IntArray =
        ProtoBufferReader(source as LenProtoRecord).packed { varint().sint32() }.toIntArray()

    // -----------------------------------------------------------------------------------------
    // ShortArray
    // -----------------------------------------------------------------------------------------

    override fun shortArray(fieldNumber: Int, fieldName: String): ShortArray =
        scalarList(fieldNumber, { value.sint32().toShort() }, { varint().sint32().toShort() }).toShortArray()

    override fun shortArrayOrNull(fieldNumber: Int, fieldName: String): ShortArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else shortArray(fieldNumber, fieldName)

    override fun rawShortArray(source: ProtoRecord): ShortArray =
        ProtoBufferReader(source as LenProtoRecord).packed { varint().sint32().toShort() }.toShortArray()

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String): ByteArray =
        get(fieldNumber)?.bytes() ?: ByteArray(0)

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byteArray(fieldNumber, fieldName)

    override fun rawByteArray(source: ProtoRecord): ByteArray {
        return source.bytes()
    }

    // -----------------------------------------------------------------------------------------
    // LongArray
    // -----------------------------------------------------------------------------------------

    override fun longArray(fieldNumber: Int, fieldName: String): LongArray =
        scalarList(fieldNumber, { value.sint64() }, { varint().sint64() }).toLongArray()

    override fun longArrayOrNull(fieldNumber: Int, fieldName: String): LongArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else longArray(fieldNumber, fieldName)

    override fun rawLongArray(source: ProtoRecord): LongArray =
        ProtoBufferReader(source as LenProtoRecord).packed { varint().sint64() }.toLongArray()

    // -----------------------------------------------------------------------------------------
    // FloatArray
    // -----------------------------------------------------------------------------------------

    override fun floatArray(fieldNumber: Int, fieldName: String): FloatArray =
        scalarList(fieldNumber, { value.float() }, { i32().float() }).toFloatArray()

    override fun floatArrayOrNull(fieldNumber: Int, fieldName: String): FloatArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else floatArray(fieldNumber, fieldName)

    override fun rawFloatArray(source: ProtoRecord): FloatArray =
        ProtoBufferReader(source as LenProtoRecord).packed { i32().float() }.toFloatArray()

    // -----------------------------------------------------------------------------------------
    // DoubleArray
    // -----------------------------------------------------------------------------------------

    override fun doubleArray(fieldNumber: Int, fieldName: String): DoubleArray =
        scalarList(fieldNumber, { value.double() }, { i64().double() }).toDoubleArray()

    override fun doubleArrayOrNull(fieldNumber: Int, fieldName: String): DoubleArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else doubleArray(fieldNumber, fieldName)

    override fun rawDoubleArray(source: ProtoRecord): DoubleArray =
        ProtoBufferReader(source as LenProtoRecord).packed { i64().double() }.toDoubleArray()

    // -----------------------------------------------------------------------------------------
    // CharArray
    // -----------------------------------------------------------------------------------------

    override fun charArray(fieldNumber: Int, fieldName: String): CharArray =
        scalarList(fieldNumber, { value.sint32().toChar() }, { varint().sint32().toChar() }).toCharArray()

    override fun charArrayOrNull(fieldNumber: Int, fieldName: String): CharArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else charArray(fieldNumber, fieldName)

    override fun rawCharArray(source: ProtoRecord): CharArray =
        ProtoBufferReader(source as LenProtoRecord).packed { varint().sint32().toChar() }.toCharArray()

    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String): String =
        get(fieldNumber)?.string() ?: ""

    override fun stringOrNull(fieldNumber: Int, fieldName: String): String? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else string(fieldNumber, fieldName)

    override fun rawString(source: ProtoRecord): String {
        return source.string()
    }

    // -----------------------------------------------------------------------------------------
    // Enum
    // -----------------------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E =
        entries[checkNotNull(get(fieldNumber)).value.toInt()]

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E? =
        get(fieldNumber)?.let { entries[it.value.toInt()] }

    override fun <E : Enum<E>> rawEnum(source: ProtoRecord, entries: EnumEntries<E>): E {
        return entries[source.value.toInt()]
    }

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    override fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T> =
        get(fieldNumber)?.uuid() ?: UUID.nil()

    override fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uuid(fieldNumber, fieldName)

    override fun <T> rawUuid(source: ProtoRecord): UUID<T> {
        return source.uuid()
    }

    // -----------------------------------------------------------------------------------------
    // UInt
    // -----------------------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String): UInt =
        get(fieldNumber)?.value?.toUInt() ?: UInt.MIN_VALUE

    override fun uIntOrNull(fieldNumber: Int, fieldName: String): UInt? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uInt(fieldNumber, fieldName)

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

    override fun rawULong(source: ProtoRecord): ULong {
        return source.value
    }

    // -----------------------------------------------------------------------------------------
    // UInt
    // -----------------------------------------------------------------------------------------

    override fun uIntArray(fieldNumber: Int, fieldName: String): UIntArray =
        scalarList(fieldNumber, { value.toUInt() }, { i32().toUInt() }).toUIntArray()

    override fun uIntArrayOrNull(fieldNumber: Int, fieldName: String): UIntArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uIntArray(fieldNumber, fieldName)

    override fun rawUIntArray(source: ProtoRecord): UIntArray =
        ProtoBufferReader(source as LenProtoRecord).packed { i32().toUInt() }.toUIntArray()

    // -----------------------------------------------------------------------------------------
    // UShortArray
    // -----------------------------------------------------------------------------------------

    override fun uShortArray(fieldNumber: Int, fieldName: String): UShortArray =
        scalarList(fieldNumber, { value.sint32().toUShort() }, { varint().sint32().toUShort() }).toUShortArray()

    override fun uShortArrayOrNull(fieldNumber: Int, fieldName: String): UShortArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uShortArray(fieldNumber, fieldName)

    override fun rawUShortArray(source: ProtoRecord): UShortArray =
        ProtoBufferReader(source as LenProtoRecord).packed { varint().sint32().toUShort() }.toUShortArray()

    // -----------------------------------------------------------------------------------------
    // UByteArray
    // -----------------------------------------------------------------------------------------

    override fun uByteArray(fieldNumber: Int, fieldName: String): UByteArray =
        scalarList(fieldNumber, { value.sint32().toUByte() }, { varint().sint32().toUByte() }).toUByteArray()

    override fun uByteArrayOrNull(fieldNumber: Int, fieldName: String): UByteArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uByteArray(fieldNumber, fieldName)

    override fun rawUByteArray(source: ProtoRecord): UByteArray =
        ProtoBufferReader(source as LenProtoRecord).packed { varint().sint32().toUByte() }.toUByteArray()

    // -----------------------------------------------------------------------------------------
    // ULongArray
    // -----------------------------------------------------------------------------------------

    override fun uLongArray(fieldNumber: Int, fieldName: String): ULongArray =
        scalarList(fieldNumber, { value }, { i64() }).toULongArray()

    override fun uLongArrayOrNull(fieldNumber: Int, fieldName: String): ULongArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uLongArray(fieldNumber, fieldName)

    override fun rawULongArray(source: ProtoRecord): ULongArray =
        ProtoBufferReader(source as LenProtoRecord).packed { i64() }.toULongArray()

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T {
        val record = get(fieldNumber) ?: return wireFormat.wireFormatDecode(null, null)
        return item(record, wireFormat)
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else instance(fieldNumber, fieldName, wireFormat)

    override fun <T> rawInstance(source: ProtoRecord, wireFormat: WireFormat<T>): T {
        return item(source, wireFormat)
    }

    override fun <T> asInstance(wireFormat: WireFormat<T>): T =
        rawInstance(records.single(), wireFormat)

    override fun <T> asInstanceOrNull(wireFormat: WireFormat<T>): T? =
        if (get(NULL_SHIFT + 1) != null) null else rawInstance(records.single(), wireFormat)

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    override fun <T1, T2> pair(
        fieldNumber: Int,
        fieldName: String,
        typeArgument1: WireFormatTypeArgument<T1>,
        typeArgument2: WireFormatTypeArgument<T2>
    ): Pair<T1?, T2?> {
        val record = checkNotNull(get(fieldNumber)) { "missing field: $fieldNumber $fieldName" }
        return rawPair(record, typeArgument1, typeArgument2)
    }

    override fun <T1, T2> pairOrNull(
        fieldNumber: Int,
        fieldName: String,
        typeArgument1: WireFormatTypeArgument<T1>,
        typeArgument2: WireFormatTypeArgument<T2>
    ): Pair<T1?, T2?>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else pair(fieldNumber, fieldName, typeArgument1, typeArgument2)

    override fun <T1, T2> rawPair(
        source: ProtoRecord,
        typeArgument1: WireFormatTypeArgument<T1>,
        typeArgument2: WireFormatTypeArgument<T2>
    ): Pair<T1?, T2?> {
        check(source is LenProtoRecord)

        val decoder = source.decoder()

        val first = if (typeArgument1.nullable && decoder[1 + NULL_SHIFT] != null) {
            null
        } else {
            (decoder[1] as LenProtoRecord).decoder()
        }

        val second = if (typeArgument2.nullable && decoder[2 + NULL_SHIFT] != null) {
            null
        } else {
            (decoder[2] as LenProtoRecord).decoder()
        }

        val firstValue = first?.let { typeArgument1.wireFormat.wireFormatDecode(first[1]!!, first) }
        val secondValue = second?.let { typeArgument2.wireFormat.wireFormatDecode(second[1]!!, second) }

        return Pair(firstValue, secondValue)
    }

    // -----------------------------------------------------------------------------------------
    // Utilities for classes that implement `WireFormat`
    // -----------------------------------------------------------------------------------------

    fun <T> item(source: ProtoRecord, wireFormat: WireFormat<T>): T =
        when (wireFormat.wireFormatKind) {
            WireFormatKind.Primitive -> {
                wireFormat.wireFormatDecode(source, this)
            }

            WireFormatKind.Collection -> {
                check(source is LenProtoRecord)
                wireFormat.wireFormatDecode(source, source.decoder())
            }

            WireFormatKind.Instance -> {
                check(source is LenProtoRecord)
                wireFormat.wireFormatDecode(source, source.decoder())
            }
        }

    override fun <T> items(source: ProtoRecord, typeArgument: WireFormatTypeArgument<T>): MutableList<T?> =
        records.map {
            if (it.fieldNumber > NULL_SHIFT) {
                check(typeArgument.nullable)
                null
            } else {
                item(it, typeArgument.wireFormat)
            }
        }.toMutableList()

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