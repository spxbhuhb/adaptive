package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.file.ensure
import `fun`.adaptive.file.exists
import `fun`.adaptive.file.list
import `fun`.adaptive.file.readString
import `fun`.adaptive.file.resolve
import `fun`.adaptive.file.write
import `fun`.adaptive.utility.*
import `fun`.adaptive.utility.UUID.Companion.monotonicUuid7
import kotlinx.coroutines.channels.Channel
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.io.readString
import kotlinx.io.writeString

/**
 * A file backed byte array queue:
 *
 * - [enqueue] enqueues a byte array
 * - [dequeue] waits until a byte array is in the queue and then returns with it
 * - [dequeueOrNull] dequeues a byte array or returns with null if the queue is empty
 * - [peek] returns with the top of the queue (waiting for it if necessary) but does not advance the dequeue position
 * - [peekOrNull] returns with the top of the queue or null if the queue is empty, but do not advance the dequeue position
 *
 * **IMPORTANT** Multiple consecutive peeks and the following dequeue returns with the **SAME** array instance. So, if
 * you modify the array returned by peek or peekOrNull, the next peeks and dequeue will return with the modified array.
 *
 * - all calls are thread-safe
 * - [enqueue] flushes the file before returning
 * - [dequeueOrNull] flushes dequeue position before returning
 * - Data is stored in chunk files, each named as `<uuid-7>.bin` or <uuid-7>.txt.
 * - The UUID is guaranteed to be monotonic, even if the clock shifts.
 *     - (assuming the latest chunk file is present during initialization)
 * - The dequeue position can be persisted with [persistDequeue] = `true`
 *     - dequeue position file is named `dequeue.txt`
 *
 * Chunk file is a sequence of records. Record structure:
 *
 * - barrier (if not empty)
 * - length (4 or 10 bytes)
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
 * @property  persistDequeue  When true, [dequeueOrNull] persists the dequeue position in a file.
 * @property  textual         When true, the queue stores the entry sizes as text (hexadecimal). Useful for
 *                            queues that can be viewed/edited with a text editor, assuming the barrier is
 *                            a new line.
 * @property  initialize      When true, [initialize] is called automatically by class `init`.
 */
class ByteArrayQueue(
    val path: Path,
    val chunkSizeLimit: Long,
    val barrier: ByteArray,
    val persistDequeue: Boolean = false,
    val textual : Boolean = false,
    initialize: Boolean = true
) {

    companion object {
        const val DEQUEUE_NAME = "dequeue.txt"
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

    internal val notificationQueue = Channel<Boolean>(2)

    private var initialized: Boolean = false

    private var peekData: ByteArray? = null

    val sizeSize : Int = if (textual) 10 else 4

    val extension = if (textual) "txt" else "bin"

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

    fun chunkFileName(uuid: UUID<*>) = "$uuid.$extension"

    val String.asChunkId
        get() = UUID<Any>(this.substringBeforeLast('.'))


    /**
     * Initialize the queue. Must be called before any call to [enqueue] and [dequeueOrNull].
     */
    fun initialize() {
        lock.use {
            if (initialized) return@use

            path.ensure()
            path.list().forEach {
                val name = it.name
                if (name.startsWith(".") || ! name.endsWith(".$extension") || name == DEQUEUE_NAME) return@forEach
                chunkIds += name.asChunkId
            }
            chunkIds.sort()

            // It is possible that the dequeue file does not exist if there hasn't been any dequeue
            // operations between creating the queue and restarting it.

            if (persistDequeue && dequeuePath.exists()) {
                val (name, position) = dequeuePath.readString().split(';')
                unsafePosition(name.asChunkId, position.toLong())
            }

            initialized = true
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun enqueue(data: ByteArray) {
        lock.use {
            ensureInitialized()

            rollEnqueueChunk(data.size)

            checkNotNull(enqueueChunk).apply {
                write(barrier)
                if (textual) {
                    writeString(data.size.toHexString(HexFormat.Default).padStart(8,'0') + "  ")
                } else {
                    writeInt(data.size)
                }
                write(data)
                flush()
            }

            enqueuePosition += data.size + barrierSize + sizeSize
            notificationQueue.trySend(true)
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

    val isNotEmpty: Boolean
        get() = ! isEmpty

    suspend fun dequeue(): ByteArray {
        var result = dequeueOrNull()

        while (result == null) {
            notificationQueue.receive()
            result = dequeueOrNull()
        }

        return result
    }

    fun dequeueOrNull(): ByteArray? {
        lock.use {
            if (peekData == null) {
                peekData = unsafePeek() ?: return null
            }
            return unSafeConsume()
        }
    }

    suspend fun peek(): ByteArray {
        var result = peekOrNull()

        while (result == null) {
            notificationQueue.receive()
            result = peekOrNull()
        }

        return result
    }

    fun peekOrNull(): ByteArray? =
        lock.use {
            peekData ?: unsafePeek()
        }

    private fun unsafePeek(): ByteArray? {
        ensureInitialized()

        rollDequeueChunk()
        val chunk = dequeueChunk ?: return null
        if (dequeuePosition >= dequeueEnd) return null

        val barrier = chunk.readByteArray(barrierSize)
        val size = if (textual) {
            chunk.readString(sizeSize.toLong()).trim().toInt(16)
        } else {
            chunk.readInt()
        }
        peekData = chunk.readByteArray(size)

        check(barrier.withIndex().all { it.value == barrier[it.index] }) { "invalid queue barrier at position $dequeuePosition in file ${path.resolve(chunkFileName(dequeueId !!))}" }

        return peekData
    }

    private fun unSafeConsume(): ByteArray {
        val safePeekData = checkNotNull(peekData)

        dequeuePosition += barrierSize + sizeSize + safePeekData.size

        if (persistDequeue) {
            dequeuePath.write("$dequeueId;$dequeuePosition", overwrite = true, useTemporaryFile = true)
        }

        peekData = null

        return safePeekData
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

            enqueuePosition = 0L
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