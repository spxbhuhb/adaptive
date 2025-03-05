package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.utility.*
import `fun`.adaptive.utility.UUID.Companion.monotonicUuid7
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

/**
 * A file backed byte array queue:
 *
 * - [enqueue] enqueues a byte array
 * - [dequeue] dequeues a byte array
 *
 * - all calls are thread-safe
 * - [enqueue] flushes the file before returning
 * - [dequeue] flushes dequeue position before returning
 * - Data is stored in chunk files, each named as `<uuid-7>.bin`.
 * - The UUID is guaranteed to be monotonic, even if the clock shifts.
 *     - (assuming the latest chunk file is present during initialization)
 * - The dequeue position can be persisted with [persistDequeue] = `true`
 *     - dequeue position file is named `dequeue.bin`
 *
 * Chunk file is a sequence of records. Record structure:
 *
 * - barrier (if not empty)
 * - length (4 bytes)
 * - data
 *
 * The barrier can be used to aid recovery, theoretically it is not necessary.
 *
 * Dequeue file is encoded in ASCII, it contains one line:
 *
 * `<dequeue-chunk-name>;<dequeue-chunk-position>`
 *
 * When [persistDequeue] is true each
 * @property  path            The path to a directory that holds queue files.
 * @property  chunkSizeLimit  Maximum size of one queue chunk file in bytes.
 * @property  barrier         A byte array to put between entries, may be empty.
 * @property  persistDequeue  When true, [dequeue] persists the dequeue position in a file.
 * @property  initialize      When true, [initialize] is called automatically by class `init`.
 */
class ByteArrayQueue(
    val path: Path,
    val chunkSizeLimit: Long,
    val barrier: ByteArray,
    val persistDequeue: Boolean = false,
    initialize: Boolean = true
) {

    companion object {
        private const val ZERO_BYTE = 0.toByte()
        const val DEQUEUE_NAME = "dequeue.bin"
    }

    private var lock = getLock()

    private var enqueueId: UUID<*>? = null
    private var enqueueChunk: Sink? = null
    private var enqueuePosition = 0L

    private var dequeueId: UUID<*>? = null
    private var dequeueChunk: Source? = null
    private var dequeuePosition: Long = 0L
    private var dequeueEnd: Long = 0L

    private val dequeuePath = path.resolve(DEQUEUE_NAME)

    private val barrierSize = barrier.size

    internal val chunkIds = mutableListOf<UUID<Any>>()

    private var initialized: Boolean = false

    val isInitialized: Boolean
        get() = lock.use { initialized }

    init {
        if (initialize) {
            initialize()
        }
    }

    fun ensureInitialized() {
        check(initialized) { "byte array queue is not initialized yet at $path" }
    }

    fun chunkFileName(uuid: UUID<*>) = "$uuid.bin"

    val String.asChunkId
        get() = UUID<Any>(this.substringBeforeLast('.'))


    /**
     * Initialize the queue. Must be called before any call to [enqueue] and [dequeue].
     */
    fun initialize() {
        lock.use {
            if (initialized) return@use

            path.ensure()
            path.list().forEach {
                val name = it.name
                if (name.startsWith(".") || ! name.endsWith(".bin") || name == DEQUEUE_NAME) return@forEach
                chunkIds += name.asChunkId
            }
            chunkIds.sort()

            if (persistDequeue && chunkIds.isNotEmpty()) {
                val (name, position) = dequeuePath.readString().split(';')
                unsafePosition(name.asChunkId, position.toLong())
            }

            initialized = true
        }
    }

    fun enqueue(data: ByteArray) {
        lock.use {
            ensureInitialized()

            rollEnqueueChunk(data.size)

            checkNotNull(enqueueChunk).apply {
                write(barrier)
                writeInt(data.size)
                write(data)
                flush()
            }

            enqueuePosition += data.size + barrierSize
        }
    }

    fun position(chunkId: UUID<Any>, position: Long) {
        lock.use {
            ensureInitialized()
            require(chunkId in chunkIds) { "unknown chunk: $chunkId" }
            unsafePosition(chunkId, position)
        }
    }

    private fun unsafePosition(chunkId: UUID<Any>, position: Long) {
        val chunkPath = path.resolve(chunkFileName(chunkId))
        val chunkSize = checkNotNull(SystemFileSystem.metadataOrNull(chunkPath)?.size) { "unable to retrieve chunk size for $chunkPath" }

        require(position <= chunkSize) { "position after end of chunk: $position" }

        dequeueId = chunkId
        dequeueChunk = SystemFileSystem.source(chunkPath).buffered().apply { skip(position) }
        dequeuePosition = position
        dequeueEnd = chunkSize
    }

    val isEmpty: Boolean
        get() = lock.use {
            ensureInitialized()
            rollDequeueChunk()
            return (dequeueChunk == null) || (dequeuePosition >= dequeueEnd)
        }

    fun dequeue(): ByteArray? {
        lock.use {
            ensureInitialized()

            rollDequeueChunk()
            val chunk = dequeueChunk ?: return null
            if (dequeuePosition >= dequeueEnd) return null

            val barrier = chunk.readByteArray(barrierSize)
            val size = chunk.readInt()
            val data = chunk.readByteArray(size)

            check(barrier.all { it == ZERO_BYTE }) { "invalid queue barrier at position $dequeuePosition in file ${path.resolve(chunkFileName(dequeueId !!))}" }

            dequeuePosition += barrierSize + 4L + size

            if (persistDequeue) {
                dequeuePath.write("$dequeueId;$dequeuePosition", overwrite = true, useTemporaryFile = true)
            }

            return data
        }
    }

    /**
     * Open new chunk if this is the first call of enqueue or if the last chunk
     * has reached chunk size limit.
     */
    private fun rollEnqueueChunk(forSize: Int) {
        if (enqueueChunk == null || enqueuePosition + forSize + barrierSize > chunkSizeLimit) {

            enqueueChunk?.close()

            val newEnqueueId = monotonicUuid7(chunkIds.lastOrNull())
            enqueueId = newEnqueueId

            val newPath = path.resolve(chunkFileName(newEnqueueId))
            enqueueChunk = SystemFileSystem.sink(newPath).buffered()
            chunkIds.add(newEnqueueId)
        }
    }


    /**
     * Position dequeue for the next read. When this function returns:
     *
     * - if dequeue is possible: [dequeuePosition < dequeueEnd]
     * - if dequeue is not possible: [dequeuePosition >= dequeueEnd]
     */
    private fun rollDequeueChunk() {

        // If we do not have a current dequeue chunk, try to use the first of
        // the known chunks.

        if (dequeueChunk == null) {
            if (chunkIds.isNotEmpty()) {
                position(chunkIds.first(), 0L)
            } else {
                return
            }
        }

        val atEnd = (dequeuePosition >= dequeueEnd)
        val sameAsEnqueue = (dequeueId == enqueueId)

        // We have remaining entries in the dequeue chunk.

        if (! atEnd) return

        // If we are at the end of the dequeue chunk we have to check if it is the
        // current enqueue chunk. If so it might be possible that we have new data.
        // No matter if we have new data or not, we can return safely as dequeue
        // handles the chunk end.

        if (atEnd && sameAsEnqueue) {
            dequeueEnd = enqueuePosition
            return
        }

        // At this point we are at the end of the old chunk. We have to close this chunk
        // and open the next one.

        dequeueChunk?.close()

        val index = chunkIds.indexOf(dequeueId)

        // If [enqueue] has not been called since initialization, this might be the last
        // known chunk. In this case just return and let dequeue handle queue end.

        if (index == chunkIds.lastIndex) return

        // At this point we can safely replace the dequeue chunk with the next one.

        position(chunkIds[index + 1], 0)
    }
}