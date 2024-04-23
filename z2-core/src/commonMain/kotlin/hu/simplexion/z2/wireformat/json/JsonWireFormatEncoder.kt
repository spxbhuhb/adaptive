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

    override fun anyList(fieldNumber: Int, fieldName: String, values: List<Any>): JsonWireFormatEncoder {
        TODO()
    }

    override fun anyListOrNull(fieldNumber: Int, fieldName: String, values: List<Any>?): JsonWireFormatEncoder {
        TODO()
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

    override fun unitList(fieldNumber: Int, fieldName: String, values: List<Unit>): JsonWireFormatEncoder {
        unitListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun unitListOrNull(fieldNumber: Int, fieldName: String, values: List<Unit>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.bool(true) }
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

    override fun booleanList(fieldNumber: Int, fieldName: String, values: List<Boolean>): JsonWireFormatEncoder {
        booleanListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun booleanListOrNull(fieldNumber: Int, fieldName: String, values: List<Boolean>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.bool(v[i]) }
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

    override fun intList(fieldNumber: Int, fieldName: String, values: List<Int>): JsonWireFormatEncoder {
        intListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun intListOrNull(fieldNumber: Int, fieldName: String, values: List<Int>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun shortList(fieldNumber: Int, fieldName: String, values: List<Short>): JsonWireFormatEncoder {
        shortListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun shortListOrNull(fieldNumber: Int, fieldName: String, values: List<Short>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun byteList(fieldNumber: Int, fieldName: String, values: List<Byte>): JsonWireFormatEncoder {
        byteListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun byteListOrNull(fieldNumber: Int, fieldName: String, values: List<Byte>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun longList(fieldNumber: Int, fieldName: String, values: List<Long>): JsonWireFormatEncoder {
        longListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun longListOrNull(fieldNumber: Int, fieldName: String, values: List<Long>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun floatList(fieldNumber: Int, fieldName: String, values: List<Float>): JsonWireFormatEncoder {
        floatListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun floatListOrNull(fieldNumber: Int, fieldName: String, values: List<Float>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun doubleList(fieldNumber: Int, fieldName: String, values: List<Double>): JsonWireFormatEncoder {
        doubleListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun doubleListOrNull(fieldNumber: Int, fieldName: String, values: List<Double>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun charList(fieldNumber: Int, fieldName: String, values: List<Char>): JsonWireFormatEncoder {
        charListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun charListOrNull(fieldNumber: Int, fieldName: String, values: List<Char>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.quotedString(v[i].toString()) }
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

    override fun stringList(fieldNumber: Int, fieldName: String, values: List<String>): JsonWireFormatEncoder {
        stringListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun stringListOrNull(fieldNumber: Int, fieldName: String, values: List<String>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.quotedString(v[i]) }
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

    override fun byteArrayList(fieldNumber: Int, fieldName: String, values: List<ByteArray>): JsonWireFormatEncoder {
        byteArrayListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun byteArrayListOrNull(fieldNumber: Int, fieldName: String, values: List<ByteArray>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.bytes(v[i]) }
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

    /**
     * Add a list of UUIDs to the message. Uses packed `bytes` to store the
     * 16 raw bytes of the UUID.
     */
    override fun uuidList(fieldNumber: Int, fieldName: String, values: List<UUID<*>>): JsonWireFormatEncoder {
        uuidListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uuidListOrNull(fieldNumber: Int, fieldName: String, values: List<UUID<*>>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.uuid(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    internal fun <T> instance(value: T, encoder: WireFormat<T>) {
        writer.openObject()
        encoder.wireFormatEncode(this, value)
        writer.closeObject()
    }

    override fun <T> instance(fieldNumber: Int, fieldName: String, value: T, encoder: WireFormat<T>): JsonWireFormatEncoder {
        instanceOrNull(fieldNumber, fieldName, value, encoder)
        return this
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, value: T?, encoder: WireFormat<T>): JsonWireFormatEncoder {
        if (value == null) {
            writer.nullValue(fieldName)
        } else {
            writer.fieldName(fieldName)
            instance(value, encoder)
        }
        return this
    }

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, values: List<T>, encoder: WireFormat<T>): JsonWireFormatEncoder {
        instanceListOrNull(fieldNumber, fieldName, values, encoder)
        return this
    }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, values: List<T>?, encoder: WireFormat<T>): JsonWireFormatEncoder {
        array(fieldName, values) { v, i ->
            encoder.wireFormatEncode(this, v[i])
            writer.rollback() // array adds the separator
        }
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

    override fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, values: List<E>, entries: EnumEntries<E>): WireFormatEncoder {
        enumListOrNull(fieldNumber, fieldName, values, entries)
        return this
    }

    override fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, values: List<E>?, entries: EnumEntries<E>): WireFormatEncoder {
        array(fieldName, values) { v, i ->
            writer.quotedString(v[i].name)
            writer.rollback() // array adds the separator
        }
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

    override fun uIntList(fieldNumber: Int, fieldName: String, values: List<UInt>): JsonWireFormatEncoder {
        uIntListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uIntListOrNull(fieldNumber: Int, fieldName: String, values: List<UInt>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun uShortList(fieldNumber: Int, fieldName: String, values: List<UShort>): JsonWireFormatEncoder {
        uShortListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uShortListOrNull(fieldNumber: Int, fieldName: String, values: List<UShort>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun uByteList(fieldNumber: Int, fieldName: String, values: List<UByte>): JsonWireFormatEncoder {
        uByteListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uByteListOrNull(fieldNumber: Int, fieldName: String, values: List<UByte>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
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

    override fun uLongList(fieldNumber: Int, fieldName: String, values: List<ULong>): JsonWireFormatEncoder {
        uLongListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uLongListOrNull(fieldNumber: Int, fieldName: String, values: List<ULong>?): JsonWireFormatEncoder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
        return this
    }
    
    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    fun <T> array(fieldName: String, values: List<T>?, block: (list: List<T>, index: Int) -> Unit) {
        if (values == null) {
            writer.nullValue(fieldName)
            return
        }

        writer.fieldName(fieldName)
        writer.openArray()
        for (index in values.indices) {
            block(values, index)
            writer.separator()
        }
        writer.closeArray()
    }

}