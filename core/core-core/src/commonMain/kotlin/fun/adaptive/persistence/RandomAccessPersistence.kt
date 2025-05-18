package `fun`.adaptive.persistence

import kotlinx.io.EOFException
import kotlin.math.min

abstract class RandomAccessPersistence : AutoCloseable {

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

    /**
     * Reads all bytes into a byte array.
     *
     * Seeks to the beginning before reading.
     *
     * @return a byte array containing all bytes
     *
     * @throws EOFException if the end-of-file is reached before reading all bytes
     */
    fun readAll() : ByteArray {
        setPosition(0)
        val bytes = ByteArray(getSize().toInt())
        read(bytes, 0, bytes.size)
        return bytes
    }

    /**
     * Processes the content of this persistence in chunks by repeatedly reading data into a buffer
     * and passing it to a processing function.
     *
     * The method starts from the beginning of the persistence, reading data in chunks of the specified size
     * until all content has been processed.
     *
     * @param chunkSize    the maximum size of each chunk to read at once
     * @param processFun   callback function that processes each chunk; receives the buffer containing the data
     *                     and the actual number of bytes read into the buffer
     */
    open fun process(chunkSize: Long, processFun: (buffer: ByteArray, size: Int) -> Unit) {
        var remaining = getSize()
        val bufferSize = min(remaining, chunkSize)
        val buffer = ByteArray(bufferSize.toInt())

        setPosition(0)

        while (remaining > 0) {
            val readSize = min(bufferSize, remaining).toInt()
            read(buffer, 0, readSize)
            processFun(buffer, readSize)
            remaining -= readSize
        }
    }

    /**
     * Writes [length] bytes of data starting at [offset] from [bytes] to the stored
     * data. The data is written starting at the current position.
     *
     * @param  bytes      the byte array to read the data from
     * @param  offset     the start offset in the array `b` of the data to write
     * @param  length     the number of bytes to write
     */
    abstract fun write(bytes: ByteArray, offset: Int, length: Int)

    /**
     * Writes the entire content of [bytes] to the stored data.
     */
    fun write(bytes: ByteArray) {
        write(bytes, 0, bytes.size)
    }

    /**
     * Returns the current offset in bytes where the next read or write occurs.
     *
     * @return the offset from the beginning of the file in bytes
     */
    abstract fun getPosition(): Long

    /**
     * Sets the position to the specified position. The next read or write occurs
     * at this position.
     *
     * @param position the offset position, measured in bytes from the beginning
     */
    abstract fun setPosition(position: Long)

    /**
     * Returns the size of this persistence in bytes.
     *
     * @return the size of this persistence in bytes
     */
    abstract fun getSize(): Long

    /**
     * Sets the size of this binary data.
     *
     * If the present size is greater than the specified size,the data will
     * be truncated.
     *
     * If the present size is less than the specified size, the data will
     * be extended.
     *
     * @param size the desired size of the data
     */
    abstract fun setSize(size: Long)

    /**
     * Closes this underlying implementation (such as a file or an SQL blob handle)
     * and releases any system resources associated with it.
     */
    abstract override fun close()

    /**
     * Deletes the data from the underlying persistence storage.
     * Calls [close] before deleting the data.
     */
    abstract fun delete()

    /**
     * Calculates the SHA-256 hash of all persistence content.
     **
     * The method moves the position to the start of the data, then reads
     * the all content in chunks and updates an SHA-256 message digest incrementally.
     *
     * This approach allows processing of large files without loading the entire content into memory.
     *
     * @param chunkSize the size of chunks to read while processing the content (default: 1MB)
     *
     * @return the resulting SHA-256 hash as a byte array
     */
    open fun sha256(chunkSize: Long = 1024L * 1024L): ByteArray {
        val digest = `fun`.adaptive.crypto.sha256()
        process(chunkSize) { buffer, size -> digest.update(buffer, 0, size) }
        return digest.digest()
    }

}