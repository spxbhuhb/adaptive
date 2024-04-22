package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.buffer.BufferWriter
import kotlin.experimental.or

/**
 * A low level protobuf writer to write information into a list of ByteArrays
 * in protobuf format. The list grows as more space needed up until the maximum
 * size set in the `maximumBufferSize` property of the companion object.
 *
 * @property  initialBufferSize     Size of the first buffer.
 * @property  additionalBufferSize  Size of the buffers that are added on-demand.
 * @property  maximumBufferSize     Maximum total size of buffers. The writer throws an exception if
 *                                  this is not enough.
 */
@Suppress("SpellCheckingInspection")
class ProtoBufferWriter(
    initialBufferSize: Int = 200,
    additionalBufferSize: Int = 10_000,
    maximumBufferSize: Int = 5_000_000 + initialBufferSize
) : BufferWriter(initialBufferSize, additionalBufferSize, maximumBufferSize) {

    // ------------------------------------------------------------------------
    // Public interface
    // ------------------------------------------------------------------------

    fun bool(fieldNumber: Int, value: Boolean) {
        tag(fieldNumber, VARINT)
        if (value) varint(1UL) else varint(0UL)
    }

    fun bool(value: Boolean) {
        if (value) varint(1UL) else varint(0UL)
    }

    fun int32(fieldNumber: Int, value: Int) {
        tag(fieldNumber, VARINT)
        varint(value.toUInt())
    }

    fun sint32(fieldNumber: Int, value: Int) {
        tag(fieldNumber, VARINT)
        varint(((value shl 1) xor (value shr 31)).toUInt())
    }

    fun sint32(value: Int) {
        varint(((value shl 1) xor (value shr 31)).toUInt())
    }

    fun uint32(fieldNumber: Int, value: UInt) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun fixed32(fieldNumber: Int, value: UInt) {
        tag(fieldNumber, I32)
        i32(value.toULong())
    }

    fun fixed32(value: UInt) {
        i32(value.toULong())
    }

    fun sfixed32(fieldNumber: Int, value: Int) {
        tag(fieldNumber, I32)
        i32(value.toULong())
    }

    fun int64(fieldNumber: Int, value: Long) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun sint64(fieldNumber: Int, value: Long) {
        tag(fieldNumber, VARINT)
        varint(((value shl 1) xor (value shr 63)).toULong())
    }

    fun sint64(value: Long) {
        varint(((value shl 1) xor (value shr 63)).toULong())
    }

    fun uint64(fieldNumber: Int, value: ULong) {
        tag(fieldNumber, VARINT)
        varint(value)
    }

    fun fixed64(fieldNumber: Int, value: ULong) {
        tag(fieldNumber, I64)
        i64(value)
    }

    fun fixed64(value: ULong) {
        i64(value)
    }

    fun sfixed64(fieldNumber: Int, value: Long) {
        tag(fieldNumber, I64)
        i64(value.toULong())
    }

    fun float(fieldNumber: Int, value: Float) {
        tag(fieldNumber, I32)
        i32(value.toRawBits().toULong())
    }

    fun double(fieldNumber: Int, value: Double) {
        tag(fieldNumber, I64)
        i64(value.toRawBits().toULong())
    }

    fun string(fieldNumber: Int, value: String) {
        tag(fieldNumber, LEN)
        string(value)
    }

    fun string(value: String) {
        val byteArray = value.encodeToByteArray()
        varint(byteArray.size.toULong())
        put(byteArray)
    }

    fun uuid(fieldNumber: Int, value: UUID<*>) {
        tag(fieldNumber, LEN)
        val byteArray = value.toByteArray()
        varint(byteArray.size.toULong())
        put(byteArray)
    }

    fun bytes(fieldNumber: Int, value: ByteArray) {
        tag(fieldNumber, LEN)
        bytes(value)
    }

    fun bytes(value: ByteArray) {
        varint(value.size.toULong())
        put(value)
    }

    // ------------------------------------------------------------------------
    // Wire format writers
    // ------------------------------------------------------------------------

    fun tag(fieldNumber: Int, type: Int) {
        val tag = (fieldNumber.toULong() shl 3) or type.toULong()
        varint(tag)
    }

    private fun i32(value: ULong): ULong {
        var remaining = value
        for (i in 0 until 4) {
            put((remaining and 0xffUL).toByte())
            remaining = remaining shr 8
        }
        return value
    }

    private fun i64(value: ULong): ULong {
        var remaining = value
        for (i in 0 until 8) {
            put((remaining and 0xffUL).toByte())
            remaining = remaining shr 8
        }
        return value
    }

    private fun varint(value: UInt) {
        var next = value and valueMaskInt
        var remaining = value shr 7

        while (remaining != 0U) {
            put(continuation or next.toByte())
            next = remaining and valueMaskInt
            remaining = remaining shr 7
        }

        put(next.toByte())
    }

    private fun varint(value: ULong) {
        var next = value and valueMaskLong
        var remaining = value shr 7

        while (remaining != 0UL) {
            put(continuation or next.toByte())
            next = remaining and valueMaskLong
            remaining = remaining shr 7
        }

        put(next.toByte())
    }

    // ------------------------------------------------------------------------
    // Constants for the wire format
    // ------------------------------------------------------------------------


    companion object {
        const val continuation = 0x80.toByte()
        const val valueMaskLong = 0x7fUL
        const val valueMaskInt = 0x7FU
    }

}