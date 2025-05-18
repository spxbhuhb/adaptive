package `fun`.adaptive.value.blob.server

import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.persistence.RandomAccessPersistence
import `fun`.adaptive.service.ServiceSessionId
import `fun`.adaptive.utility.encodeInto
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.toLong
import `fun`.adaptive.utility.use
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.blob.AvBlobUploadKey
import `fun`.adaptive.value.blob.api.AvUploadAbortException


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
class AvBlobUpload<PATH>(
    val sessionId: ServiceSessionId,
    val valueId: AvValueId,
    val key: AvBlobUploadKey,
    val size: Long,
    val dataPath : PATH,
    val dataAccess: RandomAccessPersistence,
    val statusAccess: RandomAccessPersistence,
    val logger: AdaptiveLogger = getLogger("AvBlobUpload")
) {

    /**
     * A note of a saved chunk [AvBlobUpload] uses this class to keep track of the uploaded data chunks.
     */
    class AvChunkNote(
        val offset: Long,
        val length: Long
    )

    val lock = getLock()
    
    /**
     * Principal of uploaded chunks. Updated whenever a chunk is added to the upload.
     */
    private var notes = listOf<AvChunkNote>()

    private var isAborted = false

    val isComplete
        get() = (notes.size == 1 && notes.first().offset == 0L && notes.first().length == size) || (size == 0L)

    init {
        if (dataAccess.getSize() < size) dataAccess.setSize(size)
        loadStatus()
    }

    fun pause() {
        lock.use {
            check(! isAborted) { "Upload $key is aborted" }
            dataAccess.close()
            statusAccess.close()
        }
    }

    fun close(sha256: ByteArray? = null) {
        lock.use {
            check(! isAborted) { "Upload $key is aborted" }

            if (! isComplete) abort()
            if (sha256?.contentEquals(dataAccess.sha256()) == false) abort()

            statusAccess.delete()
        }
    }

    fun add(data: ByteArray, offset: Long) {
        lock.use {
            check(! isAborted) { "Upload $key is aborted" }

            if (offset + data.size > size) abort()

            dataAccess.setPosition(offset)
            dataAccess.write(data, 0, data.size)

            addNote(offset, data.size.toLong())

            logger.fine { "upload chunk: key=$key offset=${offset} size=${data.size} notes.size=${notes.size} ${notes.joinToString { "${it.offset}:${it.length}" }}" }
        }
    }

    internal fun addNote(offset: Long, size: Long) {
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

    internal fun saveStatus() {
        val bytes = ByteArray(notes.size * 16)
        var offset = 0
        for (note in notes) {
            note.offset.encodeInto(bytes, offset)
            note.length.encodeInto(bytes, offset + 8)
            offset += 16
        }

        if (statusAccess.getSize() != bytes.size.toLong()) {
            statusAccess.setSize(bytes.size.toLong())
        }

        statusAccess.setPosition(0)
        statusAccess.write(bytes)
    }

    internal fun loadStatus() {
        val bytes = statusAccess.readAll()

        val result = mutableListOf<AvChunkNote>()
        var offset = 0

        while (offset < bytes.size) {
            result += AvChunkNote(bytes.toLong(offset), bytes.toLong(offset + 8))
            offset += 16
        }

        notes = result
    }

    internal fun abort(throwException: Boolean = true) {
        lock.use {
            if (!isAborted) {
                isAborted = true
                safeCleanup { dataAccess.delete() }
                safeCleanup { statusAccess.delete() }
            }
        }
        if (throwException) throw AvUploadAbortException()
    }

    private fun safeCleanup(block: () -> Unit) {
        try {
            block()
        } catch (ex: Exception) {
            logger.warning(ex) { "exception during cleanup, uploadId=$key, valueId=$valueId" }
        }
    }
}