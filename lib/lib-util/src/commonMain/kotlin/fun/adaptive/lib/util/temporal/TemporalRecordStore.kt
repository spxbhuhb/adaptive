package `fun`.adaptive.lib.util.temporal

import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.lib.util.temporal.model.TemporalChunk
import `fun`.adaptive.lib.util.temporal.model.TemporalChunkHeader
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.monotonicUuid7
import `fun`.adaptive.persistence.append
import `fun`.adaptive.persistence.read
import `fun`.adaptive.persistence.resolve
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class TemporalRecordStore(
    val storeUuid: UUID<TemporalRecordStore>,
    val path: Path,
    val signature: String,
    val chunkSizeLimit: Long,
    initialize: Boolean = true
) {

    private var initialized: Boolean = false

    val indexStore = TemporalIndexStore(storeUuid.cast(), path.resolve("index.pb"), initialize = false)

    private var appendId: TemporalChunkId? = null
    private var appendPath: Path? = null
    private var appendPosition = 0L

    var lastTimeStamp: Instant? = null
        private set

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
        lastTimeStamp = indexStore.latest?.timestamp
        initialized = true
    }

    fun append(timestamp: Instant, bytes: ByteArray) {
        ensureInitialized()

        val safeLastTimeStamp = lastTimeStamp

        require(safeLastTimeStamp == null || safeLastTimeStamp < timestamp) { "cannot insert out-of-bound record: $safeLastTimeStamp > $timestamp" }

        rollAppendChunk(bytes.size, timestamp)

        checkNotNull(appendPath).append(bytes)

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

        if (appendPath == null || appendPosition + forSize > chunkSizeLimit) {

            val chunkUuid = monotonicUuid7<TemporalChunk>(appendId ?: indexStore.latest?.chunk)
            appendId = chunkUuid

            val header = TemporalChunkHeader(TemporalChunkHeader.V1, chunkUuid, storeUuid, signature).encodeToProtoByteArray()

            val newPath = path.resolve(chunkFileName(chunkUuid))
            appendPath = newPath

            SystemFileSystem.sink(newPath).buffered().write(header)

            val headerSize = header.size.toLong()

            indexStore.append(TemporalIndexEntry(timestamp, chunkUuid.cast(), headerSize))
            appendPosition = headerSize

        } else {

            val latest = indexStore.latest?.timestamp ?: return

            if (timestamp.minus(1.days) > latest) {
                indexStore.append(TemporalIndexEntry(timestamp, appendId !!.cast(), appendPosition))
            }

        }
    }
}