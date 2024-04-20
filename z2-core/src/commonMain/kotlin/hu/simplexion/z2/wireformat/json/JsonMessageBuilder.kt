package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.WireFormat
import kotlin.enums.EnumEntries

/**
 * Build JSON messages.
 *
 * Use the type-specific functions to add records and then use [pack] to get
 * the wire format message.
 */
class JsonMessageBuilder(
    private val writer: JsonBufferWriter = JsonBufferWriter()
) : MessageBuilder {

    override fun pack() = writer.pack()

    override fun startInstance(): MessageBuilder {
        writer.openObject()
        return this
    }

    override fun endInstance(): MessageBuilder {
        writer.closeObject()
        return this
    }

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): JsonMessageBuilder {
        writer.bool(fieldName, value)
        return this
    }

    override fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): JsonMessageBuilder {
        writer.bool(fieldName, value)
        return this
    }

    override fun booleanList(fieldNumber: Int, fieldName: String, values: List<Boolean>): JsonMessageBuilder {
        booleanListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun booleanListOrNull(fieldNumber: Int, fieldName: String, values: List<Boolean>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.bool(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String, value: Int): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun intList(fieldNumber: Int, fieldName: String, values: List<Int>): JsonMessageBuilder {
        intListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun intListOrNull(fieldNumber: Int, fieldName: String, values: List<Int>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String, value: Long): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun longList(fieldNumber: Int, fieldName: String, values: List<Long>): JsonMessageBuilder {
        longListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun longListOrNull(fieldNumber: Int, fieldName: String, values: List<Long>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Float
    // ----------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String, value: Float): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun floatList(fieldNumber: Int, fieldName: String, values: List<Float>): JsonMessageBuilder {
        floatListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun floatListOrNull(fieldNumber: Int, fieldName: String, values: List<Float>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Double
    // ----------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String, value: Double): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    override fun doubleList(fieldNumber: Int, fieldName: String, values: List<Double>): JsonMessageBuilder {
        doubleListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun doubleListOrNull(fieldNumber: Int, fieldName: String, values: List<Double>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String, value: String): JsonMessageBuilder {
        writer.string(fieldName, value)
        return this
    }

    override fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): JsonMessageBuilder {
        writer.string(fieldName, value)
        return this
    }

    override fun stringList(fieldNumber: Int, fieldName: String, values: List<String>): JsonMessageBuilder {
        stringListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun stringListOrNull(fieldNumber: Int, fieldName: String, values: List<String>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.quotedString(v[i]) }
        return this
    }


    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): JsonMessageBuilder {
        writer.bytes(fieldName, value)
        return this
    }

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): JsonMessageBuilder {
        writer.bytes(fieldName, value)
        return this
    }

    override fun byteArrayList(fieldNumber: Int, fieldName: String, values: List<ByteArray>): JsonMessageBuilder {
        byteArrayListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun byteArrayListOrNull(fieldNumber: Int, fieldName: String, values: List<ByteArray>?): JsonMessageBuilder {
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
    override fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): JsonMessageBuilder {
        writer.uuid(fieldName, value)
        return this
    }

    override fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): JsonMessageBuilder {
        writer.uuid(fieldName, value)
        return this
    }

    /**
     * Add a list of UUIDs to the message. Uses packed `bytes` to store the
     * 16 raw bytes of the UUID.
     */
    override fun uuidList(fieldNumber: Int, fieldName: String, values: List<UUID<*>>): JsonMessageBuilder {
        uuidListOrNull(fieldNumber, fieldName, values)
        return this
    }

    override fun uuidListOrNull(fieldNumber: Int, fieldName: String, values: List<UUID<*>>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.uuid(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, value: T): JsonMessageBuilder {
        instanceOrNull(fieldNumber, fieldName, encoder, value)
        return this
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, value: T?): JsonMessageBuilder {
        if (value == null) {
            writer.nullValue(fieldName)
        } else {
            writer.fieldName(fieldName)
            encoder.encodeInstance(this, value)
        }
        return this
    }

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, values: List<T>): JsonMessageBuilder {
        instanceListOrNull(fieldNumber, fieldName, encoder, values)
        return this
    }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, encoder: WireFormat<T>, values: List<T>?): JsonMessageBuilder {
        array(fieldName, values) { v, i ->
            encoder.encodeInstance(this, v[i])
            writer.rollback() // array adds the separator
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Enum
    // ----------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E): MessageBuilder {
        enumOrNull(fieldNumber, fieldName, entries, value)
        return this
    }

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, value: E?): MessageBuilder {
        if (value == null) {
            writer.nullValue(fieldName)
        } else {
            writer.fieldName(fieldName)
            writer.quotedString(value.name)
        }
        return this
    }

    override fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>): MessageBuilder {
        enumListOrNull(fieldNumber, fieldName, entries, values)
        return this
    }

    override fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>, values: List<E>?): MessageBuilder {
        array(fieldName, values) { v, i ->
            writer.quotedString(v[i].name)
            writer.rollback() // array adds the separator
        }
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