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

    override fun anyList(fieldNumber: Int, fieldName: String, values: List<Any>): ProtoWireFormatEncoder {
        TODO()
    }

    override fun anyListOrNull(fieldNumber: Int, fieldName: String, values: List<Any>?): ProtoWireFormatEncoder {
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

    override fun unitList(fieldNumber: Int, fieldName: String, values: List<Unit>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.bool(true)
            }
        }
        return this
    }

    override fun unitListOrNull(fieldNumber: Int, fieldName: String, values: List<Unit>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            unitList(fieldNumber, fieldName, values)
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

    override fun booleanList(fieldNumber: Int, fieldName: String, values: List<Boolean>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.bool(value)
            }
        }
        return this
    }

    override fun booleanListOrNull(fieldNumber: Int, fieldName: String, values: List<Boolean>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            booleanList(fieldNumber, fieldName, values)
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

    override fun intList(fieldNumber: Int, fieldName: String, values: List<Int>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value)
            }
        }
        return this
    }

    override fun intListOrNull(fieldNumber: Int, fieldName: String, values: List<Int>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            intList(fieldNumber, fieldName, values)
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

    override fun shortList(fieldNumber: Int, fieldName: String, values: List<Short>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun shortListOrNull(fieldNumber: Int, fieldName: String, values: List<Short>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            shortList(fieldNumber, fieldName, values)
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

    override fun byteList(fieldNumber: Int, fieldName: String, values: List<Byte>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun byteListOrNull(fieldNumber: Int, fieldName: String, values: List<Byte>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            byteList(fieldNumber, fieldName, values)
        }
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

    override fun longList(fieldNumber: Int, fieldName: String, values: List<Long>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint64(value)
            }
        }
        return this
    }

    override fun longListOrNull(fieldNumber: Int, fieldName: String, values: List<Long>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            longList(fieldNumber, fieldName, values)
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

    override fun floatList(fieldNumber: Int, fieldName: String, values: List<Float>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed32(value.toBits().toUInt())
            }
        }
        return this
    }

    override fun floatListOrNull(fieldNumber: Int, fieldName: String, values: List<Float>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            floatList(fieldNumber, fieldName, values)
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

    override fun doubleList(fieldNumber: Int, fieldName: String, values: List<Double>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed64(value.toBits().toULong())
            }
        }
        return this
    }

    override fun doubleListOrNull(fieldNumber: Int, fieldName: String, values: List<Double>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            doubleList(fieldNumber, fieldName, values)
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

    override fun charList(fieldNumber: Int, fieldName: String, values: List<Char>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.code)
            }
        }
        return this
    }

    override fun charListOrNull(fieldNumber: Int, fieldName: String, values: List<Char>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            charList(fieldNumber, fieldName, values)
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

    override fun stringList(fieldNumber: Int, fieldName: String, values: List<String>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.string(value)
            }
        }
        return this
    }

    override fun stringListOrNull(fieldNumber: Int, fieldName: String, values: List<String>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            stringList(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawString(value: String): WireFormatEncoder {
        writer.string(value)
        return this
    }

    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

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

    override fun byteArrayList(fieldNumber: Int, fieldName: String, values: List<ByteArray>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.bytes(value)
            }
        }
        return this
    }

    override fun byteArrayListOrNull(fieldNumber: Int, fieldName: String, values: List<ByteArray>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            byteArrayList(fieldNumber, fieldName, values)
        }
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

    /**
     * Add a list of UUIDs to the message. Uses packed `bytes` to store the
     * 16 raw bytes of the UUID.
     */
    override fun uuidList(fieldNumber: Int, fieldName: String, values: List<UUID<*>>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.bytes(value.toByteArray())
            }
        }
        return this
    }

    override fun uuidListOrNull(fieldNumber: Int, fieldName: String, values: List<UUID<*>>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uuidList(fieldNumber, fieldName, values)
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

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, values: List<T>, encoder: WireFormat<T>): ProtoWireFormatEncoder {
        for (value in values) {
            instance(fieldNumber, fieldName, value, encoder)
        }
        return this
    }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, values: List<T>?, encoder: WireFormat<T>): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            for (value in values) {
                instance(fieldNumber, fieldName, value, encoder)
            }
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

    override fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, values: List<E>, entries: EnumEntries<E>): WireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.ordinal)
            }
        }
        return this
    }

    override fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, values: List<E>?, entries: EnumEntries<E>): WireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            intList(fieldNumber, fieldName, values.map { it.ordinal })
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

    override fun uIntList(fieldNumber: Int, fieldName: String, values: List<UInt>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed32(value)
            }
        }
        return this
    }

    override fun uIntListOrNull(fieldNumber: Int, fieldName: String, values: List<UInt>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uIntList(fieldNumber, fieldName, values)
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

    override fun uShortList(fieldNumber: Int, fieldName: String, values: List<UShort>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun uShortListOrNull(fieldNumber: Int, fieldName: String, values: List<UShort>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uShortList(fieldNumber, fieldName, values)
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

    override fun uByteList(fieldNumber: Int, fieldName: String, values: List<UByte>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun uByteListOrNull(fieldNumber: Int, fieldName: String, values: List<UByte>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uByteList(fieldNumber, fieldName, values)
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

    override fun uLongList(fieldNumber: Int, fieldName: String, values: List<ULong>): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed64(value)
            }
        }
        return this
    }

    override fun uLongListOrNull(fieldNumber: Int, fieldName: String, values: List<ULong>?): ProtoWireFormatEncoder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uLongList(fieldNumber, fieldName, values)
        }
        return this
    }

    override fun rawULong(value: ULong): WireFormatEncoder {
        writer.fixed64(value)
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