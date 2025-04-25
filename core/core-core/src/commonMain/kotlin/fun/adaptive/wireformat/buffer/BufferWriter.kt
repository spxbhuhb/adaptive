/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.buffer

import kotlin.math.max

/**
 * A low level writer to write information into a list of ByteArrays. The list grows
 * as more space needed up until the maximum size set in the `maximumBufferSize` property
 * of the companion object.
 *
 * @property  initialBufferSize     Size of the first buffer.
 * @property  additionalBufferSize  Size of the buffers that are added on-demand.
 * @property  maximumBufferSize     Maximum total size of buffers. The writer throws an exception if
 *                                  this is not enough.
 */
open class BufferWriter(
    val initialBufferSize: Int = 200,
    val additionalBufferSize: Int = 10_000,
    val maximumBufferSize: Int = 50_000_000 + initialBufferSize
) {

    /**
     * Sum of valuable byte counts in all buffers but the last.
     */
    private var pastBufferByteCount = 0

    /**
     * Write offset in the current buffer.
     */
    private var writeOffset = 0

    private val buffers = mutableListOf(ByteArray(initialBufferSize))

    // FIXME write unit tests for buffer recycle
    private val recycle = mutableListOf<ByteArray>()

    private var buffer = buffers.last()

    /**
     * Number of bytes written.
     */
    val size
        get() = pastBufferByteCount + writeOffset

    /**
     * Reset the writer:
     *
     * - moves all but the first buffer to [recycle]
     * - sets offsets to 0
     * - fills the first buffer with zeros
     *
     * Meant for optimization, so the same writer can be reused without allocating the
     * buffers again and again.
     */
    fun reset() {
        pastBufferByteCount = 0
        writeOffset = 0
        recycle += buffers.drop(1)
        val first = buffers.first()
        buffers.clear()
        buffers += first
        first.fill(0) // this is for security, just in case
    }

    /**
     * Pack all the buffers into one.
     */
    fun pack(): ByteArray {
        val data = ByteArray(size)
        var offset = 0
        val last = buffers.last()
        for (buffer in buffers) {
            if (buffer !== last) {
                buffer.copyInto(data, offset)
            } else {
                buffer.copyInto(data, offset, 0, writeOffset)
            }
            offset += buffer.size
        }
        return data
    }

    protected fun put(byte: Byte) {
        if (writeOffset == buffer.size) {
            addBuffer()
        }

        buffer[writeOffset ++] = byte
    }

    /**
     * If [byteArray] fits into the current buffer, copy it into the current
     * buffer.
     *
     * If [byteArray] does not fit into the current buffer, fill all the available
     * part and then allocate a new buffer. The new buffer will be big enough (if
     * [maximumBufferSize] allows) to hold all the remaining data.
     */
    protected open fun put(byteArray: ByteArray) {
        val availableSpace = buffer.size - writeOffset
        var copyOffset = 0
        val length = byteArray.size

        if (length > availableSpace) {
            byteArray.copyInto(buffer, writeOffset, 0, availableSpace)
            copyOffset = availableSpace
            addBuffer(max(additionalBufferSize, length - availableSpace))
        }

        byteArray.copyInto(buffer, writeOffset, copyOffset, length)
        writeOffset += length - copyOffset
    }

    /**
     * Add a new buffer to the list of buffers and set it as the current buffer.
     */
    private fun addBuffer(requestedSize: Int = additionalBufferSize) {
        pastBufferByteCount += buffer.size

        check(pastBufferByteCount + requestedSize < maximumBufferSize) { "ProtoBufferWriter buffer overflow $pastBufferByteCount + $requestedSize > $maximumBufferSize" }

        if (recycle.isNotEmpty()) {
            buffer = recycle.removeLast()
            buffer.fill(0)
        } else {
            buffer = ByteArray(requestedSize)
        }

        buffers.add(buffer)
        writeOffset = 0
    }

    /**
     * Rolls back the write position by 1. Drops the last buffer when it is empty.
     */
    fun rollback() {
        if (writeOffset == 0) {
            buffers.removeLast()
            writeOffset = buffers.last().size - 1
        } else {
            writeOffset -= 1
        }
    }

    /**
     * Get the last byte written into the buffer.
     */
    fun peekLast() : Byte {
        if (writeOffset == 0) {
            check(buffers.size != 1) { "write buffer underflow" }
            return buffers[buffers.size - 2].last()
        } else {
            return buffers.last()[writeOffset - 1]
        }
    }

}