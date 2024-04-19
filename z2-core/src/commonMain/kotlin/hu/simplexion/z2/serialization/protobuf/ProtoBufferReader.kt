package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.toUuid

/**
 * A low level protobuf reader that decodes the protobuf wire
 * format into a list of protobuf records.
 *
 * At this level it is not possible to destructure the message
 * more than one level as the reader has no information about
 * the internal structure of LEN fields.
 *
 * @property   buffer  Data to read.
 * @property   offset  Offset of the first byte in [buffer] this reader should process.
 * @property   length  Number of bytes available for this reader. There may be more bytes
 *                     in the buffer, but the reader does not read over the length.
 */
class ProtoBufferReader(
    val buffer: ByteArray,
    val offset: Int = 0,
    val length: Int = buffer.size
) {

    constructor(record: LenProtoRecord) : this(record.byteArray, record.offset, record.length)

    /**
     * Convert the byte array into a list of [ProtoRecord]. The records, [LenProtoRecord]s
     * in particular, are backed by the buffer, you should not modify the buffer while
     * they are used.
     */
    fun records(): List<ProtoRecord> {
        val records = mutableListOf<ProtoRecord>()

        readOffset = offset
        val readEnd = offset + length

        while (readOffset < readEnd) {
            val tag = varint()
            val fieldNumber = (tag shr 3).toInt()
            val type = (tag and 7UL).toInt()

            records += when (type) {
                VARINT -> VarintProtoRecord(fieldNumber, varint())
                I64 -> I64ProtoRecord(fieldNumber, i64())
                I32 -> I32ProtoRecord(fieldNumber, i32())
                LEN -> {
                    val length = varint().toInt()
                    LenProtoRecord(fieldNumber, buffer, readOffset, length).also {
                        readOffset += length
                    }
                }

                else -> throw IllegalArgumentException("unknown type $type")
            }
        }

        // the reader should read all data, not less, not more
        check(readOffset - offset == length) { "read length mismatch, structural problem in the message or software bug" }

        return records
    }

    /**
     * Read packed lists.
     */
    fun <T> packed(item: ProtoBufferReader.() -> T): List<T> {
        readOffset = offset
        val readEnd = offset + length

        val list = mutableListOf<T>()

        while (readOffset < readEnd) {
            list += item()
        }

        // the reader should read all data, not less, not more
        check(readOffset - offset == length) { "read length mismatch, structural problem in the message or software bug" }

        return list
    }

    private var endOffset = offset + length
    private var readOffset = offset

    private fun get(): ULong {
        check(readOffset < endOffset)
        return buffer[readOffset ++].toULong() and 0xffUL
    }

    fun i32(): ULong {
        var value = 0UL
        for (i in 0 until 4) {
            value = value or (get() shl (i * 8))
        }
        return value
    }

    fun i64(): ULong {
        var value = 0UL
        for (i in 0 until 8) {
            value = value or (get() shl (i * 8))
        }
        return value
    }

    fun string(): String {
        val length = varint().toInt()
        val value = buffer.decodeToString(readOffset, readOffset + length)
        readOffset += length
        return value
    }

    fun <T> uuid(): UUID<T> {
        val length = varint().toInt()
        check(length == 16)
        val value = buffer.toUuid<T>(readOffset)
        readOffset += length
        return value
    }

    fun bytes(): ByteArray {
        val length = varint().toInt()
        val value = buffer.copyOfRange(readOffset, readOffset + length)
        readOffset += length
        return value
    }

    fun varint(): ULong {
        var next = get()
        var shift = 0
        var value = 0UL

        while ((next and continuation) != 0UL) {
            value = value or ((next and valueMask) shl shift)
            next = get()
            shift += 7
        }

        return value or ((next and valueMask) shl shift)
    }

}