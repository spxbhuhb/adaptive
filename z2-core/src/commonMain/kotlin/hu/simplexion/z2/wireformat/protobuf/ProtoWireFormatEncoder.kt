package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatEncoder
import kotlin.enums.EnumEntries

/**
 * Build Protocol Buffer messages.
 *
 * Use the type-specific functions to add records and then use [pack] to get
 * the wire format message.
 */
@ExperimentalUnsignedTypes
class ProtoWireFormatEncoder : WireFormatEncoder {

    val writer = ProtoBufferWriter()

    override fun pack() = writer.pack()

    // ----------------------------------------------------------------------------
    // Any
    // ----------------------------------------------------------------------------

    override fun any(fieldNumber: Int, fieldName: String, value: Any): ProtoWireFormatEncoder {
        TODO()
    }

    override fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): ProtoWireFormatEncoder {
        TODO()
    }

    override fun rawAny(value: Any): WireFormatEncoder {
        TODO()
    }

    // ----------------------------------------------------------------------------
    // Unit
    // ----------------------------------------------------------------------------

    override fun unit(fieldNumber: Int, fieldName: String, value: Unit): ProtoWireFormatEncoder {
        writer.bool(fieldNumber, true)
        return this
    }

    override fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bool(fieldNumber, true)
        }
        return this
    }

    override fun rawUnit(value: Unit): WireFormatEncoder {
        writer.bool(true)
        return this
    }

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): ProtoWireFormatEncoder {
        writer.bool(fieldNumber, value)
        return this
    }

    override fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bool(fieldNumber, value)
        }
        return this
    }

    override fun booleanArray(fieldNumber: Int, fieldName: String, values: BooleanArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.bool(value)
            }
        }
        return this
    }

    override fun booleanArrayOrNull(fieldNumber: Int, fieldName: String, values: BooleanArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            booleanArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawBoolean(value: Boolean): WireFormatEncoder {
        writer.bool(true)
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String, value: Int): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value)
        return this
    }

    override fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value)
        }
        return this
    }

    override fun intArray(fieldNumber: Int, fieldName: String, values: IntArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value)
            }
        }
        return this
    }

    override fun intArrayOrNull(fieldNumber: Int, fieldName: String, values: IntArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            intArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawInt(value: Int): WireFormatEncoder {
        writer.sint32(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Short
    // ----------------------------------------------------------------------------

    override fun short(fieldNumber: Int, fieldName: String, value: Short): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun shortArray(fieldNumber: Int, fieldName: String, values: ShortArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun shortArrayOrNull(fieldNumber: Int, fieldName: String, values: ShortArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            shortArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawShort(value: Short): WireFormatEncoder {
        writer.sint32(value.toInt())
        return this
    }

    // ----------------------------------------------------------------------------
    // Byte
    // ----------------------------------------------------------------------------

    override fun byte(fieldNumber: Int, fieldName: String, value: Byte): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): ProtoWireFormatEncoder {
        writer.bytes(fieldNumber, value)
        return this
    }

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bytes(fieldNumber, value)
        }
        return this
    }

    override fun rawByteArray(value: ByteArray): WireFormatEncoder {
        writer.bytes(value)
        return this
    }

    override fun rawByte(value: Byte): WireFormatEncoder {
        writer.sint32(value.toInt())
        return this
    }

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String, value: Long): ProtoWireFormatEncoder {
        writer.sint64(fieldNumber, value)
        return this
    }

    override fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint64(fieldNumber, value)
        }
        return this
    }

    override fun longArray(fieldNumber: Int, fieldName: String, values: LongArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint64(value)
            }
        }
        return this
    }

    override fun longArrayOrNull(fieldNumber: Int, fieldName: String, values: LongArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            longArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawLong(value: Long): WireFormatEncoder {
        writer.sint64(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Float
    // ----------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String, value: Float): ProtoWireFormatEncoder {
        writer.fixed32(fieldNumber, value.toBits().toUInt())
        return this
    }

    override fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed32(fieldNumber, value.toBits().toUInt())
        }
        return this
    }

    override fun floatArray(fieldNumber: Int, fieldName: String, values: FloatArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed32(value.toBits().toUInt())
            }
        }
        return this
    }

    override fun floatArrayOrNull(fieldNumber: Int, fieldName: String, values: FloatArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            floatArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawFloat(value: Float): WireFormatEncoder {
        writer.fixed32(value.toBits().toUInt())
        return this
    }

    // ----------------------------------------------------------------------------
    // Double
    // ----------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String, value: Double): ProtoWireFormatEncoder {
        writer.fixed64(fieldNumber, value.toBits().toULong())
        return this
    }

    override fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed64(fieldNumber, value.toBits().toULong())
        }
        return this
    }

    override fun doubleArray(fieldNumber: Int, fieldName: String, values: DoubleArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed64(value.toBits().toULong())
            }
        }
        return this
    }

    override fun doubleArrayOrNull(fieldNumber: Int, fieldName: String, values: DoubleArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            doubleArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawDouble(value: Double): WireFormatEncoder {
        writer.fixed64(value.toBits().toULong())
        return this
    }

    // ----------------------------------------------------------------------------
    // Char
    // ----------------------------------------------------------------------------

    override fun char(fieldNumber: Int, fieldName: String, value: Char): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.code)
        return this
    }

    override fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.code)
        }
        return this
    }

    override fun charArray(fieldNumber: Int, fieldName: String, values: CharArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.code)
            }
        }
        return this
    }

    override fun charArrayOrNull(fieldNumber: Int, fieldName: String, values: CharArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            charArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawChar(value: Char): WireFormatEncoder {
        writer.sint32(value.code)
        return this
    }

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String, value: String): ProtoWireFormatEncoder {
        writer.string(fieldNumber, value)
        return this
    }

    override fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.string(fieldNumber, value)
        }
        return this
    }

    override fun rawString(value: String): WireFormatEncoder {
        writer.string(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    /**
     * Add a UUID to the message. Uses `bytes` to store the 16 raw bytes of
     * the UUID.
     */
    override fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): ProtoWireFormatEncoder {
        writer.bytes(fieldNumber, value.toByteArray())
        return this
    }

    override fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bytes(fieldNumber, value.toByteArray())
        }
        return this
    }

    override fun rawUuid(value: UUID<*>): WireFormatEncoder {
        writer.bytes(value.toByteArray())
        return this
    }

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, value: T, encoder: WireFormat<T>): ProtoWireFormatEncoder {
        val bytes = ProtoWireFormatEncoder().apply { encoder.wireFormatEncode(this, value) }.pack()
        writer.bytes(fieldNumber, bytes)
        return this
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, value: T?, encoder: WireFormat<T>): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            instance(fieldNumber, fieldName, value, encoder)
        }
        return this
    }

    override fun <T> rawInstance(value: T, wireFormat: WireFormat<T>): WireFormatEncoder {
        val bytes = ProtoWireFormatEncoder().apply { wireFormat.wireFormatEncode(this, value) }.pack()
        writer.bytes(bytes)
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, value: E, entries: EnumEntries<E>): WireFormatEncoder {
        writer.sint32(fieldNumber, value.ordinal)
        return this
    }

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, value: E?, entries: EnumEntries<E>): WireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.ordinal)
        }
        return this
    }

    override fun <E : Enum<E>> rawEnum(value: E, entries: EnumEntries<E>): WireFormatEncoder {
        writer.sint32(value.ordinal)
        return this
    }

    // ----------------------------------------------------------------------------
    // UInt
    // ----------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String, value: UInt): ProtoWireFormatEncoder {
        writer.fixed32(fieldNumber, value)
        return this
    }

    override fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed32(fieldNumber, value)
        }
        return this
    }

    override fun uIntArray(fieldNumber: Int, fieldName: String, values: UIntArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed32(value)
            }
        }
        return this
    }

    override fun uIntArrayOrNull(fieldNumber: Int, fieldName: String, values: UIntArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uIntArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawUInt(value: UInt): WireFormatEncoder {
        writer.fixed32(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // UShort
    // ----------------------------------------------------------------------------

    override fun uShort(fieldNumber: Int, fieldName: String, value: UShort): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun uShortArray(fieldNumber: Int, fieldName: String, values: UShortArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun uShortArrayOrNull(fieldNumber: Int, fieldName: String, values: UShortArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uShortArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawUShort(value: UShort): WireFormatEncoder {
        writer.sint32(value.toInt())
        return this
    }

    // ----------------------------------------------------------------------------
    // UByte
    // ----------------------------------------------------------------------------

    override fun uByte(fieldNumber: Int, fieldName: String, value: UByte): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun uByteArray(fieldNumber: Int, fieldName: String, values: UByteArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun uByteArrayOrNull(fieldNumber: Int, fieldName: String, values: UByteArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uByteArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawUByte(value: UByte): WireFormatEncoder {
        writer.sint32(value.toInt())
        return this
    }

    // ----------------------------------------------------------------------------
    // ULong
    // ----------------------------------------------------------------------------

    override fun uLong(fieldNumber: Int, fieldName: String, value: ULong): ProtoWireFormatEncoder {
        writer.fixed64(fieldNumber, value)
        return this
    }

    override fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed64(fieldNumber, value)
        }
        return this
    }

    override fun uLongArray(fieldNumber: Int, fieldName: String, values: ULongArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed64(value)
            }
        }
        return this
    }

    override fun uLongArrayOrNull(fieldNumber: Int, fieldName: String, values: ULongArray?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uLongArray(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawULong(value: ULong): WireFormatEncoder {
        writer.fixed64(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Collection
    // ----------------------------------------------------------------------------

    override fun <T> collection(fieldNumber: Int, fieldName: String, values: Collection<T>, wireFormat: WireFormat<T>): ProtoWireFormatEncoder {
        for (value in values) {
            instance(fieldNumber, fieldName, value, wireFormat)
        }
        return this
    }

    override fun <T> collectionOrNull(fieldNumber: Int, fieldName: String, values: Collection<T>?, wireFormat: WireFormat<T>): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            for (value in values) {
                instance(fieldNumber, fieldName, value, wireFormat)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    fun sub(fieldNumber: Int, block: (sub: ProtoBufferWriter) -> Unit) {
        val sub = ProtoBufferWriter()
        block(sub)
        writer.bytes(fieldNumber, sub.pack())
    }

}