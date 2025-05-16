package `fun`.adaptive.value.blob.server

import `fun`.adaptive.crypto.sha256
import `fun`.adaptive.file.RandomAccessFile
import `fun`.adaptive.file.delete
import `fun`.adaptive.file.getRandomAccess
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.ServiceSessionId
import `fun`.adaptive.utility.encodeInto
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.toLong
import `fun`.adaptive.utility.use
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.blob.AvBlobUploadKey
import `fun`.adaptive.value.blob.api.AvUploadAbortException
import kotlinx.io.files.Path
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.min


/**
 * An upload process.
 *
 * UploadStart lifecycle:
 *
 * ```text
 *    start
 *    pause - system restart, move node etc.
 *    start
 *    pause - system restart, move node etc.
 *    ...
 *    start
 *    close
 *```
 *
 * @param   size     The size of the complete content. The upload is done when there is
 *                   only one entry in [notes] with an offset of `0` and length equals
 *                   [size].
 */
class AvBlobUpload(
    val sessionId : ServiceSessionId,
    val valueId : AvValueId,
    val key: AvBlobUploadKey,
    val size: Long,
    val dataPath: Path,
    val statusPath: Path,
    val logger : AdaptiveLogger = getLogger("AvBlobUpload")
) {

    /**
     * A note of a saved chunk [AvBlobUpload] uses this class to keep track of the uploaded data chunks.
     */
    class AvChunkNote(
        val offset : Long,
        val length: Long
    )

    val lock = getLock()

    val dataFile = dataPath.getRandomAccess("rw")
    val statusFile = statusPath.getRandomAccess("rw")

    /**
     * Principal of uploaded chunks. Updated whenever a chunk is added to the upload.
     */
    private var notes = listOf<AvChunkNote>()

    val isComplete
        get() = (notes.size == 1 && notes.first().offset == 0L && notes.first().length == size) || (size == 0L)

    init {
        if (dataFile.length() < size) dataFile.setLength(size)
        loadStatus()
    }

    fun pause() {
        dataFile.close()
        statusFile.close()
    }

    fun close(sha256: String) {
        if (! isComplete) abort()
        // FIXME sha if (sha256 != dataFile.sha256()) abort()

        statusPath.delete(mustExists = false)
    }

    fun add(data : ByteArray, offset : Long) {
        lock.use {
            if (offset + data.size > size) abort()

            dataFile.seek(offset)
            dataFile.write(data, 0, data.size)

            addNote(offset, data.size.toLong())

            logger.fine { "upload chunk: key=$key offset=${offset} size=${data.size} notes.size=${notes.size} ${notes.joinToString { "${it.offset}:${it.length}" }}" }
        }
    }

    fun addNote(offset: Long, size: Long) {
        val all = (notes + AvChunkNote(offset, size)).sortedBy { it.offset }
        val merged = mutableListOf<AvChunkNote>()

        for (note in all) {
            if (merged.isEmpty()) {
                merged += note
                continue
            }

            val last = merged.last()

            if (last.offset + last.length == note.offset) {
                merged[merged.size - 1] = AvChunkNote(last.offset, last.length + note.length)
            } else {
                merged += note
            }
        }

        notes = merged
        saveStatus()
    }

    fun saveStatus() {
        val bytes = ByteArray(notes.size * 16)
        var offset = 0
        for (note in notes) {
            note.offset.encodeInto(bytes, offset)
            note.length.encodeInto(bytes, offset + 8)
            offset += 16
        }

        if (statusFile.length() != bytes.size.toLong()) {
            statusFile.setLength(bytes.size.toLong())
        }

        statusFile.seek(0)
        statusFile.write(bytes)
    }

    fun loadStatus() {
        val bytes = statusFile.readAll()

        val result = mutableListOf<AvChunkNote>()
        var offset = 0

        while (offset < bytes.size) {
            result += AvChunkNote(bytes.toLong(offset), bytes.toLong(offset + 8))
            offset += 16
        }

        notes = result
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun RandomAccessFile.calcSha256(): String {
        val digest = sha256()

        var remaining = length()

        val bufferSize = min(remaining, 1024L * 1024L)
        val bytes = ByteArray(bufferSize.toInt())

        seek(0)
        while (remaining > 0) {
            val readSize = min(bufferSize, remaining).toInt()
            val actualRead = read(bytes, 0, readSize)
            digest.update(bytes, 0, actualRead)
            remaining -= actualRead
        }

        return Base64.encode(digest.digest())
    }

    fun abort(throwException: Boolean = true) {
        safeCleanup { dataFile.close() }
        safeCleanup { statusFile.close() }
        safeCleanup { dataPath.delete(mustExists = false) }
        safeCleanup { statusPath.delete() }
        if (throwException) throw AvUploadAbortException()
    }

    fun safeCleanup(block: () -> Unit) {
        try {
            block()
        } catch (ex: Exception) {
            // TODO logging
            ex.printStackTrace()
        }
    }
}