package `fun`.adaptive.lib.util.temporal

import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.monotonicUuid7
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.resolve
import kotlinx.datetime.Instant
import kotlinx.io.Sink
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.time.Duration.Companion.days

class TemporalRecordStore(
    val uuid: UUID<TemporalRecordStore>,
    val path: Path,
    val chunkSizeLimit: Long,
    initialize: Boolean = true
) {

    private var initialized: Boolean = false

    val indexStore = TemporalIndexStore(uuid.cast(), path.resolve("index.pb"), initialize = false)

    var appendId: UUID<Any>? = null
    var appendChunk: Sink? = null
    var appendPosition = 0L

    var lastTimeStamp: Instant? = null

    init {
        if (initialize) {
            initialize()
        }
    }

    fun ensureInitialized() {
        check(initialized) { "byte array queue is not initialized yet at $path" }
    }

    fun chunkFileName(uuid: UUID<*>) = "$uuid.pb"

    fun initialize() {
        if (initialized) return

        indexStore.initialize()
        initialized = true
    }

    fun append(timestamp: Instant, bytes: ByteArray) {
        ensureInitialized()

        val safeLastTimeStamp = lastTimeStamp

        require(safeLastTimeStamp == null || safeLastTimeStamp < timestamp) { "cannot insert out-of-band record: $safeLastTimeStamp > $timestamp" }

        rollAppendChunk(bytes.size, timestamp)

        checkNotNull(appendChunk).apply {
            write(bytes)
            flush()
        }

        lastTimeStamp = timestamp
        appendPosition += bytes.size
    }

    fun query(start: Instant, end: Instant): List<ByteArray> {
        ensureInitialized()

        val chunks = chunksFor(start, end, lastTimeStamp, indexStore.entries)
        if (chunks.isEmpty()) return emptyList()

        val result = mutableListOf<ByteArray>()
        for (chunk in chunks) {
            val path = path.resolve(chunkFileName(chunk))
            result += path.read()
        }

        return result
    }

    /**
     * Open new chunk if this is the first call of enqueue or if the last chunk
     * has reached chunk size limit.
     */
    private fun rollAppendChunk(forSize: Int, timestamp: Instant) {

        if (appendChunk == null || appendPosition + forSize > chunkSizeLimit) {
            appendChunk?.close()

            val newId = monotonicUuid7(appendId ?: indexStore.latest?.chunk).cast<Any>()
            appendId = newId

            val newPath = path.resolve(chunkFileName(newId))
            appendChunk = SystemFileSystem.sink(newPath).buffered()
            indexStore.append(TemporalIndexEntry(timestamp, newId.cast(), 0))

        } else {

            val latest = indexStore.latest?.timestamp ?: return
            if (timestamp.minus(1.days) < latest) {
                indexStore.append(TemporalIndexEntry(timestamp, appendId !!.cast(), 0))
            }

        }
    }
}