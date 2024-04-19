package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.serialization.InstanceEncoder
import hu.simplexion.z2.serialization.MessageBuilder
import hu.simplexion.z2.util.UUID

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

    override fun <T> instance(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, value: T): ProtoMessageBuilder {
        writer.bytes(fieldNumber, encoder.encodeInstance(ProtoMessageBuilder(), value))
        return this
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, value: T?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bytes(fieldNumber, encoder.encodeInstance(ProtoMessageBuilder(), value))
        }
        return this
    }

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, values: List<T>): ProtoMessageBuilder {
        for (value in values) {
            writer.bytes(fieldNumber, encoder.encodeInstance(ProtoMessageBuilder(), value))
        }
        return this
    }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, encoder: InstanceEncoder<T>, values: List<T>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            for (value in values) {
                writer.bytes(fieldNumber, encoder.encodeInstance(ProtoMessageBuilder(), value))
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

    override fun subBuilder() = ProtoMessageBuilder()
}