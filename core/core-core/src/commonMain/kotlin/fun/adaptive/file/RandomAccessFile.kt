package `fun`.adaptive.file

import kotlinx.io.EOFException

abstract class RandomAccessFile : AutoCloseable {

    /**
     * Reads `len` bytes of data starting at offset `off` from the input into the specified byte array `b`.
     * This method blocks until some input is available or an end-of-file is reached.
     *
     * @param  bytes      the byte array to store the read data
     * @param  offset     the start offset in the array `b` where the data will be written
     * @param  length     the maximum number of bytes to read
     * @param  exception  if true (default), throws an `EOFException` when the end of the file is reached prematurely
     *
     * @return the total number of bytes read into the buffer
     *
     * @throws EOFException if the end-of-file is reached before reading the specified number of bytes and [exception] is true
     */
    abstract fun read(bytes: ByteArray, offset: Int, length: Int, exception: Boolean = true): Int

    fun readAll() : ByteArray {
        seek(0)
        val bytes = ByteArray(length().toInt())
        read(bytes, 0, bytes.size)
        return bytes
    }

    abstract fun write(bytes: ByteArray, offset: Int, length: Int)

    fun write(bytes: ByteArray) {
        write(bytes, 0, bytes.size)
    }

    abstract fun seek(position: Long)

    abstract fun getFilePointer(): Long

    abstract fun length(): Long

    abstract fun setLength(length: Long)

    abstract override fun close()

}