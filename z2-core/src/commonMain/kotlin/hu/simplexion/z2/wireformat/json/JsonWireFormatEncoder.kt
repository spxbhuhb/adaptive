package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatEncoder
import kotlin.enums.EnumEntries

/**
 * Build JSON messages.
 *
 * Use the type-specific functions to add records and then use [pack] to get
 * the wire format message.
 */
@OptIn(ExperimentalUnsignedTypes::class)
class JsonWireFormatEncoder(
    private val writer: JsonBufferWriter = JsonBufferWriter()
) : WireFormatEncoder {

    override fun pack() = writer.pack()

    // ----------------------------------------------------------------------------
    // Any
    // ----------------------------------------------------------------------------

    override fun any(fieldNumber: Int, fieldName: String, value: Any): JsonWireFormatEncoder {
        TODO()
    }

    override fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): JsonWireFormatEncoder {
        TODO()
    }

    override fun rawAny(value: Any): WireFormatEncoder {
        TODO("Not yet implemented")
    }

    // ----------------------------------------------------------------------------
    // Unit
    // ----------------------------------------------------------------------------

    override fun unit(fieldNumber: Int, fieldName: String, value: Unit): JsonWireFormatEncoder {
        writer.bool(fieldName, true)
        return this
    }

    override fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): JsonWireFormatEncoder {
        writer.bool(fieldName, if (value != null) true else null)
        return this
    }

    override fun rawUnit(value: Unit): WireFormatEncoder {
        writer.bool(true)
        return this
    }

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): JsonWireFormatEncoder {
        writer.bool(fieldName, value)
        return this
    }

    override fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): JsonWireFormatEncoder {
        writer.bool(fieldName, value)
        return this
    }

    override fun booleanArray(fieldNumber: Int, fieldName: String, values: BooleanArray): JsonWireFormatEncoder {
        booleanArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun booleanArrayOrNull(fieldNumber: Int, fieldName: String, values: BooleanArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.bool(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawBoolean(value: Boolean): WireFormatEncoder {
        writer.bool(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String, value: Int): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun intArray(fieldNumber: Int, fieldName: String, values: IntArray): JsonWireFormatEncoder {
        intArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun intArrayOrNull(fieldNumber: Int, fieldName: String, values: IntArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawInt(value: Int): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Short
    // ----------------------------------------------------------------------------

    override fun short(fieldNumber: Int, fieldName: String, value: Short): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun shortArray(fieldNumber: Int, fieldName: String, values: ShortArray): JsonWireFormatEncoder {
        shortArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun shortArrayOrNull(fieldNumber: Int, fieldName: String, values: ShortArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawShort(value: Short): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Byte
    // ----------------------------------------------------------------------------

    override fun byte(fieldNumber: Int, fieldName: String, value: Byte): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }


    override fun rawByte(value: Byte): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String, value: Long): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun longArray(fieldNumber: Int, fieldName: String, values: LongArray): JsonWireFormatEncoder {
        longArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun longArrayOrNull(fieldNumber: Int, fieldName: String, values: LongArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawLong(value: Long): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Float
    // ----------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String, value: Float): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun floatArray(fieldNumber: Int, fieldName: String, values: FloatArray): JsonWireFormatEncoder {
        floatArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun floatArrayOrNull(fieldNumber: Int, fieldName: String, values: FloatArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawFloat(value: Float): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Double
    // ----------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String, value: Double): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun doubleArray(fieldNumber: Int, fieldName: String, values: DoubleArray): JsonWireFormatEncoder {
        doubleArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun doubleArrayOrNull(fieldNumber: Int, fieldName: String, values: DoubleArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawDouble(value: Double): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Char
    // ----------------------------------------------------------------------------

    override fun char(fieldNumber: Int, fieldName: String, value: Char): JsonWireFormatEncoder {
        writer.string(fieldName, value.toString())
        return this
    }

    override fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): JsonWireFormatEncoder {
        writer.string(fieldName, value?.toString())
        return this
    }

    override fun charArray(fieldNumber: Int, fieldName: String, values: CharArray): JsonWireFormatEncoder {
        charArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun charArrayOrNull(fieldNumber: Int, fieldName: String, values: CharArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.string(value.toString())
                writer.separator()
            }
        }
        return this
    }

    override fun rawChar(value: Char): WireFormatEncoder {
        writer.quotedString(value.toString())
        return this

    }
    
    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String, value: String): JsonWireFormatEncoder {
        writer.string(fieldName, value)
        return this
    }

    override fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): JsonWireFormatEncoder {
        writer.string(fieldName, value)
        return this
    }

    override fun rawString(value: String): WireFormatEncoder {
        writer.quotedString(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): JsonWireFormatEncoder {
        writer.bytes(fieldName, value)
        return this
    }

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): JsonWireFormatEncoder {
        writer.bytes(fieldName, value)
        return this
    }

    override fun rawByteArray(value: ByteArray): WireFormatEncoder {
        writer.bytes(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    /**
     * Add a UUID to the message. Uses `bytes` to store the 16 raw bytes of
     * the UUID.
     */
    override fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): JsonWireFormatEncoder {
        writer.uuid(fieldName, value)
        return this
    }

    override fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): JsonWireFormatEncoder {
        writer.uuid(fieldName, value)
        return this
    }

    override fun rawUuid(value: UUID<*>): WireFormatEncoder {
        writer.uuid(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, value: T, encoder: WireFormat<T>): JsonWireFormatEncoder {
        instanceOrNull(fieldNumber, fieldName, value, encoder)
        return this
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, value: T?, encoder: WireFormat<T>): JsonWireFormatEncoder {
        if (value == null) {
            writer.nullValue(fieldName)
        } else {
            writer.fieldName(fieldName)
            rawInstance(value, encoder)
        }
        return this
    }

    override fun <T> rawInstance(value: T, wireFormat: WireFormat<T>): WireFormatEncoder {
        writer.openObject()
        wireFormat.wireFormatEncode(this, value)
        writer.closeObject()
        return this
    }

    // ----------------------------------------------------------------------------
    // Enum
    // ----------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, value: E, entries: EnumEntries<E>): WireFormatEncoder {
        enumOrNull(fieldNumber, fieldName, value, entries)
        return this
    }

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, value: E?, entries: EnumEntries<E>): WireFormatEncoder {
        if (value == null) {
            writer.nullValue(fieldName)
        } else {
            writer.fieldName(fieldName)
            writer.quotedString(value.name)
        }
        return this
    }

    override fun <E : Enum<E>> rawEnum(value: E, entries: EnumEntries<E>): WireFormatEncoder {
        writer.quotedString(value.name)
        return this
    }

    // ----------------------------------------------------------------------------
    // UInt
    // ----------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String, value: UInt): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uIntArray(fieldNumber: Int, fieldName: String, values: UIntArray): JsonWireFormatEncoder {
        uIntArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uIntArrayOrNull(fieldNumber: Int, fieldName: String, values: UIntArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawUInt(value: UInt): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // UShort
    // ----------------------------------------------------------------------------

    override fun uShort(fieldNumber: Int, fieldName: String, value: UShort): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uShortArray(fieldNumber: Int, fieldName: String, values: UShortArray): JsonWireFormatEncoder {
        uShortArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uShortArrayOrNull(fieldNumber: Int, fieldName: String, values: UShortArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawUShort(value: UShort): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // UByte
    // ----------------------------------------------------------------------------

    override fun uByte(fieldNumber: Int, fieldName: String, value: UByte): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uByteArray(fieldNumber: Int, fieldName: String, values: UByteArray): JsonWireFormatEncoder {
        uByteArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uByteArrayOrNull(fieldNumber: Int, fieldName: String, values: UByteArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawUByte(value: UByte): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // ULong
    // ----------------------------------------------------------------------------

    override fun uLong(fieldNumber: Int, fieldName: String, value: ULong): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): JsonWireFormatEncoder {
        writer.number(fieldName, value)
        return this
    }

    override fun uLongArray(fieldNumber: Int, fieldName: String, values: ULongArray): JsonWireFormatEncoder {
        uLongArrayOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uLongArrayOrNull(fieldNumber: Int, fieldName: String, values: ULongArray?): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                writer.number(value)
                writer.separator()
            }
        }
        return this
    }

    override fun rawULong(value: ULong): WireFormatEncoder {
        writer.number(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // Collection
    // ----------------------------------------------------------------------------

    override fun <T> collection(fieldNumber: Int, fieldName: String, values: Collection<T>, wireFormat: WireFormat<T>): JsonWireFormatEncoder {
        collectionOrNull(fieldNumber, fieldName, values, wireFormat)
        return this
    }

    override fun <T> collectionOrNull(fieldNumber: Int, fieldName: String, values: Collection<T>?, wireFormat: WireFormat<T>): JsonWireFormatEncoder {
        array(fieldName, values) {
            for (value in values !!) {
                wireFormat.wireFormatEncode(this, value)
                writer.separator()
            }
            writer.rollback() // array adds the separator
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    fun <T> array(fieldName: String, values: T?, block: (values: T) -> Unit) {
        if (values == null) {
            writer.nullValue(fieldName)
            return
        }

        writer.fieldName(fieldName)
        writer.openArray()
        block(values)
        writer.closeArray()
    }

}