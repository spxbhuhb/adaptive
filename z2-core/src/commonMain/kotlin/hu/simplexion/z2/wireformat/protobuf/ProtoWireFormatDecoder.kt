package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder

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
) : WireFormatDecoder {

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

    override fun anyList(fieldNumber: Int, fieldName: String) =
        TODO()

    override fun anyListOrNull(fieldNumber: Int, fieldName: String): List<Any> =
        TODO()

    // -----------------------------------------------------------------------------------------
    // Unit
    // -----------------------------------------------------------------------------------------

    override fun unit(fieldNumber: Int, fieldName: String) {
        check(get(fieldNumber)?.value == 1UL)
    }

    override fun unitOrNull(fieldNumber: Int, fieldName: String): Unit? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else unit(fieldNumber, fieldName)

    override fun unitList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { check(value.bool()) }, { check(varint().bool()) })

    override fun unitListOrNull(fieldNumber: Int, fieldName: String): List<Unit>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else unitList(fieldNumber, fieldName)
    
    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String): Boolean =
        get(fieldNumber)?.let { it.value == 1UL } ?: false

    override fun booleanOrNull(fieldNumber: Int, fieldName: String): Boolean? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else boolean(fieldNumber, fieldName)

    override fun booleanList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.bool() }, { varint().bool() })

    override fun booleanListOrNull(fieldNumber: Int, fieldName: String): List<Boolean>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else booleanList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String): Int =
        get(fieldNumber)?.value?.sint32() ?: 0

    override fun intOrNull(fieldNumber: Int, fieldName: String): Int? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else int(fieldNumber, fieldName)

    override fun intList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.sint32() }, { varint().sint32() })

    override fun intListOrNull(fieldNumber: Int, fieldName: String): List<Int>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else intList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // Short
    // -----------------------------------------------------------------------------------------

    override fun short(fieldNumber: Int, fieldName: String): Short =
        get(fieldNumber)?.value?.sint32()?.toShort() ?: 0

    override fun shortOrNull(fieldNumber: Int, fieldName: String): Short? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else short(fieldNumber, fieldName)

    override fun shortList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.sint32().toShort() }, { varint().sint32().toShort() })

    override fun shortListOrNull(fieldNumber: Int, fieldName: String): List<Short>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else shortList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // Byte
    // -----------------------------------------------------------------------------------------

    override fun byte(fieldNumber: Int, fieldName: String): Byte =
        get(fieldNumber)?.value?.sint32()?.toByte() ?: 0

    override fun byteOrNull(fieldNumber: Int, fieldName: String): Byte? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byte(fieldNumber, fieldName)

    override fun byteList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.sint32().toByte() }, { varint().sint32().toByte() })

    override fun byteListOrNull(fieldNumber: Int, fieldName: String): List<Byte>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byteList(fieldNumber, fieldName)
    
    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String): Long =
        get(fieldNumber)?.value?.sint64() ?: 0L

    override fun longOrNull(fieldNumber: Int, fieldName: String): Long? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else long(fieldNumber, fieldName)

    override fun longList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.sint64() }, { varint().sint64() })

    override fun longListOrNull(fieldNumber: Int, fieldName: String): List<Long>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else longList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // Float
    // -----------------------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String): Float = get(fieldNumber)?.value?.float() ?: 0f

    override fun floatOrNull(fieldNumber: Int, fieldName: String): Float? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else float(fieldNumber, fieldName)

    override fun floatList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.float() }, { i32().float() })

    override fun floatListOrNull(fieldNumber: Int, fieldName: String): List<Float>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else floatList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // Double
    // -----------------------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String): Double = get(fieldNumber)?.value?.double() ?: 0.0

    override fun doubleOrNull(fieldNumber: Int, fieldName: String): Double? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else double(fieldNumber, fieldName)

    override fun doubleList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.double() }, { i64().double() })

    override fun doubleListOrNull(fieldNumber: Int, fieldName: String): List<Double>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else doubleList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // Char
    // -----------------------------------------------------------------------------------------

    override fun char(fieldNumber: Int, fieldName: String): Char =
        get(fieldNumber)?.value?.sint32()?.toChar() ?: Char.MIN_VALUE

    override fun charOrNull(fieldNumber: Int, fieldName: String): Char? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else char(fieldNumber, fieldName)

    override fun charList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.sint32().toChar() }, { varint().sint32().toChar() })

    override fun charListOrNull(fieldNumber: Int, fieldName: String): List<Char>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else charList(fieldNumber, fieldName)
    
    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String): String = get(fieldNumber)?.string() ?: ""

    override fun stringOrNull(fieldNumber: Int, fieldName: String): String? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else string(fieldNumber, fieldName)

    override fun stringList(fieldNumber: Int, fieldName: String) = scalarList(fieldNumber, { string() }, { string() })

    override fun stringListOrNull(fieldNumber: Int, fieldName: String): List<String>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else stringList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String): ByteArray = get(fieldNumber)?.bytes() ?: ByteArray(0)

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byteArray(fieldNumber, fieldName)

    override fun byteArrayList(fieldNumber: Int, fieldName: String) = scalarList(fieldNumber, { bytes() }, { bytes() })

    override fun byteArrayListOrNull(fieldNumber: Int, fieldName: String): List<ByteArray>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else byteArrayList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    override fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T> = get(fieldNumber)?.uuid() ?: UUID.nil()

    override fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uuid(fieldNumber, fieldName)

    override fun <T> uuidList(fieldNumber: Int, fieldName: String): List<UUID<T>> = scalarList(fieldNumber, { uuid() }, { uuid() })

    override fun <T> uuidListOrNull(fieldNumber: Int, fieldName: String): List<UUID<T>>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uuidList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T {
        val record = get(fieldNumber) ?: return decoder.wireFormatDecode(null)
        check(record is LenProtoRecord)
        return decoder.wireFormatDecode(record.message())
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else instance(fieldNumber, fieldName, decoder)

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): MutableList<T> {
        val list = mutableListOf<T>()
        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue
            check(record is LenProtoRecord)
            list += decoder.wireFormatDecode(record.message())
        }
        return list
    }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): List<T>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else instanceList(fieldNumber, fieldName, decoder)


    // -----------------------------------------------------------------------------------------
    // UInt
    // -----------------------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String): UInt =
        get(fieldNumber)?.value?.toUInt() ?: UInt.MIN_VALUE

    override fun uIntOrNull(fieldNumber: Int, fieldName: String): UInt? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uInt(fieldNumber, fieldName)

    override fun uIntList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.toUInt() }, { i32().toUInt() })

    override fun uIntListOrNull(fieldNumber: Int, fieldName: String): List<UInt>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uIntList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // UShort
    // -----------------------------------------------------------------------------------------

    override fun uShort(fieldNumber: Int, fieldName: String): UShort =
        get(fieldNumber)?.value?.sint32()?.toUShort() ?: UShort.MIN_VALUE

    override fun uShortOrNull(fieldNumber: Int, fieldName: String): UShort? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uShort(fieldNumber, fieldName)

    override fun uShortList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.sint32().toUShort() }, { varint().sint32().toUShort() })

    override fun uShortListOrNull(fieldNumber: Int, fieldName: String): List<UShort>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uShortList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // UByte
    // -----------------------------------------------------------------------------------------

    override fun uByte(fieldNumber: Int, fieldName: String): UByte =
        get(fieldNumber)?.value?.sint32()?.toUByte() ?: UByte.MIN_VALUE

    override fun uByteOrNull(fieldNumber: Int, fieldName: String): UByte? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uByte(fieldNumber, fieldName)

    override fun uByteList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value.sint32().toUByte() }, { varint().sint32().toUByte() })

    override fun uByteListOrNull(fieldNumber: Int, fieldName: String): List<UByte>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uByteList(fieldNumber, fieldName)

    // -----------------------------------------------------------------------------------------
    // ULong
    // -----------------------------------------------------------------------------------------

    override fun uLong(fieldNumber: Int, fieldName: String): ULong =
        get(fieldNumber)?.value ?: ULong.MIN_VALUE

    override fun uLongOrNull(fieldNumber: Int, fieldName: String): ULong? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uLong(fieldNumber, fieldName)

    override fun uLongList(fieldNumber: Int, fieldName: String) =
        scalarList(fieldNumber, { value }, { i64() })

    override fun uLongListOrNull(fieldNumber: Int, fieldName: String): List<ULong>? =
        if (get(fieldNumber + NULL_SHIFT) != null) null else uLongList(fieldNumber, fieldName)
    
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