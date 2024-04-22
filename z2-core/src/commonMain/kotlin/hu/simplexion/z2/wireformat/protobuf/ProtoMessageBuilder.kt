package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.WireFormat
import kotlin.enums.EnumEntries

/**
 * Build Protocol Buffer messages.
 *
 * Use the type-specific functions to add records and then use [pack] to get
 * the wire format message.
 */
class ProtoMessageBuilder : MessageBuilder {

    val writer = ProtoBufferWriter()

    override fun pack() = writer.pack()

    override fun startInstance(): MessageBuilder {
        return this // protobuf is length based
    }

    override fun endInstance(): MessageBuilder {
        return this // protobuf is length based
    }

    // ----------------------------------------------------------------------------
    // Any
    // ----------------------------------------------------------------------------

    override fun any(fieldNumber: Int, fieldName: String, value: Any): ProtoMessageBuilder {
        TODO()
    }

    override fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): ProtoMessageBuilder {
        TODO()
    }

    override fun anyList(fieldNumber: Int, fieldName: String, values: List<Any>): ProtoMessageBuilder {
        TODO()
    }

    override fun anyListOrNull(fieldNumber: Int, fieldName: String, values: List<Any>?): ProtoMessageBuilder {
        TODO()
    }

    // ----------------------------------------------------------------------------
    // Unit
    // ----------------------------------------------------------------------------

    override fun unit(fieldNumber: Int, fieldName: String, value: Unit): ProtoMessageBuilder {
        writer.bool(fieldNumber, true)
        return this
    }

    override fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bool(fieldNumber, true)
        }
        return this
    }

    override fun unitList(fieldNumber: Int, fieldName: String, values: List<Unit>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bool(true)
            }
        }
        return this
    }

    override fun unitListOrNull(fieldNumber: Int, fieldName: String, values: List<Unit>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            unitList(fieldNumber, fieldName, values)
        }
        return this
    }
    
    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): ProtoMessageBuilder {
        writer.bool(fieldNumber, value)
        return this
    }

    override fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bool(fieldNumber, value)
        }
        return this
    }

    override fun booleanList(fieldNumber: Int, fieldName: String, values: List<Boolean>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bool(value)
            }
        }
        return this
    }

    override fun booleanListOrNull(fieldNumber: Int, fieldName: String, values: List<Boolean>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            booleanList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String, value: Int): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value)
        return this
    }

    override fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value)
        }
        return this
    }

    override fun intList(fieldNumber: Int, fieldName: String, values: List<Int>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value)
            }
        }
        return this
    }

    override fun intListOrNull(fieldNumber: Int, fieldName: String, values: List<Int>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            intList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Short
    // ----------------------------------------------------------------------------

    override fun short(fieldNumber: Int, fieldName: String, value: Short): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun shortList(fieldNumber: Int, fieldName: String, values: List<Short>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun shortListOrNull(fieldNumber: Int, fieldName: String, values: List<Short>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            shortList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Byte
    // ----------------------------------------------------------------------------

    override fun byte(fieldNumber: Int, fieldName: String, value: Byte): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun byteList(fieldNumber: Int, fieldName: String, values: List<Byte>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun byteListOrNull(fieldNumber: Int, fieldName: String, values: List<Byte>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            byteList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String, value: Long): ProtoMessageBuilder {
        writer.sint64(fieldNumber, value)
        return this
    }

    override fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint64(fieldNumber, value)
        }
        return this
    }

    override fun longList(fieldNumber: Int, fieldName: String, values: List<Long>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint64(value)
            }
        }
        return this
    }

    override fun longListOrNull(fieldNumber: Int, fieldName: String, values: List<Long>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            longList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Float
    // ----------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String, value: Float): ProtoMessageBuilder {
        writer.fixed32(fieldNumber, value.toBits().toUInt())
        return this
    }

    override fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed32(fieldNumber, value.toBits().toUInt())
        }
        return this
    }

    override fun floatList(fieldNumber: Int, fieldName: String, values: List<Float>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed32(value.toBits().toUInt())
            }
        }
        return this
    }

    override fun floatListOrNull(fieldNumber: Int, fieldName: String, values: List<Float>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            floatList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Double
    // ----------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String, value: Double): ProtoMessageBuilder {
        writer.fixed64(fieldNumber, value.toBits().toULong())
        return this
    }

    override fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed64(fieldNumber, value.toBits().toULong())
        }
        return this
    }

    override fun doubleList(fieldNumber: Int, fieldName: String, values: List<Double>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed64(value.toBits().toULong())
            }
        }
        return this
    }

    override fun doubleListOrNull(fieldNumber: Int, fieldName: String, values: List<Double>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            doubleList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Char
    // ----------------------------------------------------------------------------

    override fun char(fieldNumber: Int, fieldName: String, value: Char): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value.code)
        return this
    }

    override fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.code)
        }
        return this
    }

    override fun charList(fieldNumber: Int, fieldName: String, values: List<Char>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.code)
            }
        }
        return this
    }

    override fun charListOrNull(fieldNumber: Int, fieldName: String, values: List<Char>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            charList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String, value: String): ProtoMessageBuilder {
        writer.string(fieldNumber, value)
        return this
    }

    override fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.string(fieldNumber, value)
        }
        return this
    }

    override fun stringList(fieldNumber: Int, fieldName: String, values: List<String>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.string(value)
            }
        }
        return this
    }

    override fun stringListOrNull(fieldNumber: Int, fieldName: String, values: List<String>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            stringList(fieldNumber, fieldName, values)
        }
        return this
    }


    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): ProtoMessageBuilder {
        writer.bytes(fieldNumber, value)
        return this
    }

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bytes(fieldNumber, value)
        }
        return this
    }

    override fun byteArrayList(fieldNumber: Int, fieldName: String, values: List<ByteArray>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bytes(value)
            }
        }
        return this
    }

    override fun byteArrayListOrNull(fieldNumber: Int, fieldName: String, values: List<ByteArray>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            byteArrayList(fieldNumber, fieldName, values)
        }
        return this
    }


    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    /**
     * Add a UUID to the message. Uses `bytes` to store the 16 raw bytes of
     * the UUID.
     */
    override fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): ProtoMessageBuilder {
        writer.bytes(fieldNumber, value.toByteArray())
        return this
    }

    override fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): ProtoMessageBuilder {
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
    override fun uuidList(fieldNumber: Int, fieldName: String, values: List<UUID<*>>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bytes(value.toByteArray())
            }
        }
        return this
    }

    override fun uuidListOrNull(fieldNumber: Int, fieldName: String, values: List<UUID<*>>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uuidList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, value: T): ProtoMessageBuilder {
        val bytes = ProtoMessageBuilder().apply { encoder.encodeInstance(this, value) }.pack()
        writer.bytes(fieldNumber, bytes)
        return this
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, value: T?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            instance(fieldNumber, fieldName, encoder, value)
        }
        return this
    }

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, values: List<T>): ProtoMessageBuilder {
        for (value in values) {
            instance(fieldNumber, fieldName, encoder, value)
        }
        return this
    }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, values: List<T>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            for (value in values) {
                instance(fieldNumber, fieldName, encoder, value)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E): MessageBuilder {
        writer.sint32(fieldNumber, value.ordinal)
        return this
    }

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E?): MessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.ordinal)
        }
        return this
    }

    override fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>): MessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.ordinal)
            }
        }
        return this
    }

    override fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>?): MessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            intList(fieldNumber, fieldName, values.map { it.ordinal })
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // UInt
    // ----------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String, value: UInt): ProtoMessageBuilder {
        writer.fixed32(fieldNumber, value)
        return this
    }

    override fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed32(fieldNumber, value)
        }
        return this
    }

    override fun uIntList(fieldNumber: Int, fieldName: String, values: List<UInt>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed32(value)
            }
        }
        return this
    }

    override fun uIntListOrNull(fieldNumber: Int, fieldName: String, values: List<UInt>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uIntList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // UShort
    // ----------------------------------------------------------------------------

    override fun uShort(fieldNumber: Int, fieldName: String, value: UShort): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun uShortList(fieldNumber: Int, fieldName: String, values: List<UShort>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun uShortListOrNull(fieldNumber: Int, fieldName: String, values: List<UShort>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uShortList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // UByte
    // ----------------------------------------------------------------------------

    override fun uByte(fieldNumber: Int, fieldName: String, value: UByte): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun uByteList(fieldNumber: Int, fieldName: String, values: List<UByte>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value.toInt())
            }
        }
        return this
    }

    override fun uByteListOrNull(fieldNumber: Int, fieldName: String, values: List<UByte>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uByteList(fieldNumber, fieldName, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // ULong
    // ----------------------------------------------------------------------------

    override fun uLong(fieldNumber: Int, fieldName: String, value: ULong): ProtoMessageBuilder {
        writer.fixed64(fieldNumber, value)
        return this
    }

    override fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed64(fieldNumber, value)
        }
        return this
    }

    override fun uLongList(fieldNumber: Int, fieldName: String, values: List<ULong>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.fixed64(value)
            }
        }
        return this
    }

    override fun uLongListOrNull(fieldNumber: Int, fieldName: String, values: List<ULong>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uLongList(fieldNumber, fieldName, values)
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