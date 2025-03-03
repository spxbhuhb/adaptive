package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.utility.*
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlin.time.Duration.Companion.minutes

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

    val ZERO_BYTE = 0.toByte()

    var lock = getLock()

    var enqueueName: String? = null
    var enqueueChunk: Sink? = null
    var enqueuePosition = 0L

    var dequeueName: String? = null
    var dequeueChunk: Source? = null
    var dequeuePosition: Long = 0L
    var dequeueEnd: Long = 0L

    val dequeuePath = path.resolve("dequeue.bin")

    val barrierSize = barrier.size

    val chunkNames = mutableListOf<String>()

    var initialized: Boolean = false
        get() = lock.use { field }
        private set(value) = lock.use { field = value }

    init {
        if (initialize) {
            initialize()
        }
    }

    /**
     * Initialize the queue. Must be called before any call to [enqueue] and [dequeue].
     */
    fun initialize() {
        lock.use {
            path.ensure()
            path.list().forEach {
                val name = it.name
                if (name.startsWith(".") || ! name.endsWith(".bin")) return@forEach
                chunkNames.add(it.name)
            }
            chunkNames.sort()

            if (persistDequeue && chunkNames.isNotEmpty()) {
                val (name, position) = dequeuePath.readString().split(';')
                position(name, position.toLong())
            }

            initialized = true
        }
    }

    fun enqueue(data: ByteArray) {
        lock.use {
            check(initialized) { "byte array queue is not initialized yet at $path" }

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

    fun position(chunkName: String, position: Long) {
        lock.use {
            require(chunkName in chunkNames) { "unknown chunk name: $chunkName" }

            val chunkPath = path.resolve(chunkName)
            val chunkSize = checkNotNull(SystemFileSystem.metadataOrNull(chunkPath)?.size) { "unable to retrieve chunk size for $chunkPath" }

            require(position < chunkSize) { "position after end of chunk: $position" }

            dequeueName = chunkName
            dequeueChunk = SystemFileSystem.source(chunkPath).buffered().apply { skip(position) }
            dequeuePosition = position
            dequeueEnd = chunkSize
        }
    }

    fun dequeue(): ByteArray? {
        lock.use {
            rollDequeueChunk()
            val chunk = dequeueChunk ?: return null
            if (dequeuePosition >= dequeueEnd) return null

            val barrier = chunk.readByteArray(barrierSize)
            val size = chunk.readInt()
            val data = chunk.readByteArray(size)

            check(barrier.all { it == ZERO_BYTE }) { "invalid queue barrier at position $dequeuePosition in file ${path.resolve(dequeueName !!)}" }

            dequeuePosition += barrierSize + 4L + size

            if (persistDequeue) {
                dequeuePath.write("$dequeueName;$dequeuePosition", overwrite = true, useTemporaryFile = true)
            }

            return data
        }
    }

    /**
     * Open new chunk if this is the first call of enqueue or if the last chunk
     * has reached chunk size limit.
     */
    fun rollEnqueueChunk(forSize: Int) {
        if (enqueueChunk == null || enqueuePosition + forSize + barrierSize > chunkSizeLimit) {

            enqueueChunk?.close()

            enqueueName = monotonicChunkName()

            val newPath = path.resolve(enqueueName !!)
            enqueueChunk = SystemFileSystem.sink(newPath).buffered()
            chunkNames.add(newPath.name)
        }
    }

    /**
     * Generate a new chunk name that is guaranteed to be larger than any
     * previously known chunk names. This might be in the future in case the
     * clock was set back.
     */
    fun monotonicChunkName(): String {
        var startTime = now()

        var newName = "${uuid7<Any>(startTime, secureRandom(3))}.bin"

        while (chunkNames.lastOrNull()?.let { it > newName } == true) {
            startTime += 1.minutes
            newName = "${uuid7<Any>(startTime, secureRandom(3))}.bin"
        }

        return newName
    }

    /**
     * Position dequeue for the next read. When this function returns:
     *
     * - if dequeue is possible: [dequeuePosition < dequeueEnd]
     * - if dequeue is not possible: [dequeuePosition >= dequeueEnd]
     */
    fun rollDequeueChunk() {

        // If we do not have a current dequeue chunk, try to use the first of
        // the known chunks.

        if (dequeueChunk == null) {
            if (chunkNames.isNotEmpty()) {
                position(chunkNames.first(), 0L)
            } else {
                return
            }
        }

        val atEnd = (dequeuePosition >= dequeueEnd)
        val sameAsEnqueue = (dequeueName == enqueueName)

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
        dequeueChunk = null

        val index = chunkNames.indexOf(dequeueName)

        // If [enqueue] has not been called since initialization, this might be the last
        // known chunk. In this case just return and let dequeue handle queue end.

        if (index == chunkNames.lastIndex) return

        // At this point we can safely replace the dequeue chunk with the next one.

        position(chunkNames[index + 1], 0)
    }
}