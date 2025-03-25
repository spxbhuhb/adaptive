package `fun`.adaptive.iot.history.backend

import `fun`.adaptive.adat.decodeFromJson
import `fun`.adaptive.adat.decodeFromProto
import `fun`.adaptive.adat.encodeToJsonByteArray
import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.iot.history.model.AioHistoryAddQueueEntry
import `fun`.adaptive.iot.history.model.AioHistoryId
import `fun`.adaptive.iot.history.model.AioHistoryMetadata
import `fun`.adaptive.iot.history.model.AioHistoryQuery
import `fun`.adaptive.lib.util.bytearray.ByteArrayQueue
import `fun`.adaptive.lib.util.path.UuidFileStore
import `fun`.adaptive.lib.util.temporal.TemporalRecordStore
import `fun`.adaptive.utility.*
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.io.files.Path
import kotlin.js.JsName
import kotlin.time.Duration.Companion.minutes

class AioHistoryWorker : WorkerImpl<AioHistoryWorker> {

    companion object {
        const val META_NAME = "meta.json"
        const val CHUNK_SIZE = 1024L * 1024L
    }

    val historyRoot by setting<Path> { "AIO_HISTORY_PATH" }

    val historyAccessLock = getLock()

    val contexts = mutableMapOf<AioHistoryId, AioHistoryAccessContext>()

    val histories = mutableMapOf<AioHistoryId, AioHistoryRuntimeInfo>()

    lateinit var store : UuidFileStore<Unit>

    private var initialized: Boolean = false

    private val addQueue = ByteArrayQueue(
        historyRoot.resolve("queue.add"),
        16 * 1024 * 1024,
        ByteArray(20) { 0 },
        persistDequeue = true
    )

    private val errorQueue = ByteArrayQueue(
        historyRoot.resolve("queue.error"),
        16 * 1024 * 1024,
        ByteArray(20) { 0 },
        persistDequeue = true
    )

    private val outOfBoundQueue = ByteArrayQueue(
        historyRoot.resolve("queue.out-of-bound"),
        16 * 1024 * 1024,
        ByteArray(20) { 0 },
        persistDequeue = true
    )

    override suspend fun run() {
        store = object : UuidFileStore<Unit>(historyRoot, 2, dirStore = true) {
            override fun loadPath(path: Path, map: Unit) {
                loadHistory(path)?.let {
                    contexts[it.metadata.uuid] = AioHistoryAccessContext(it.metadata.uuid)
                    histories[it.metadata.uuid] = it
                }
            }
        }

        historyAccessLock.use {
            store.loadAll(Unit)
            initialized = true
        }

        while (isActive) {
            processAddQueue()
        }
    }

    // --------------------------------------------------------------------------------
    // Access synchronization
    // --------------------------------------------------------------------------------

    val isInitialized
        get() = historyAccessLock.use { initialized }

    val isIdle
        get() = addQueue.isEmpty

    suspend fun <T> runOperation(uuid: AioHistoryId, block: suspend () -> T): T {
        acquireForOperation(uuid)
        try {
            return block()
        } finally {
            releaseFromOperation(uuid)
        }
    }

    suspend fun acquireForOperation(uuid: AioHistoryId) {
        waitFor(1.minutes) {
            historyAccessLock.use {
                val context = checkNotNull(contexts[uuid]) { "no context for history id: $uuid" }
                (! context.mergeSemaphore).also { if (it) context.runningOperationCount += 1 }
            }
        }
    }

    fun releaseFromOperation(uuid: AioHistoryId) {
        historyAccessLock.use {
            val context = checkNotNull(contexts[uuid]) { "no context for history id: $uuid" }
            context.runningOperationCount -= 1
        }
    }

    suspend fun runMerge(uuid: AioHistoryId, block: suspend () -> Unit) {
        acquireForMerge(uuid)
        try {
            block()
        } finally {
            releaseFromMerge(uuid)
        }
    }

    suspend fun acquireForMerge(uuid: AioHistoryId) {
        waitFor(1.minutes) {
            historyAccessLock.use {
                val context = checkNotNull(contexts[uuid]) { "no context for history id: $uuid" }
                context.mergeSemaphore = true
                context.runningOperationCount == 0
            }
        }
    }

    fun releaseFromMerge(uuid: AioHistoryId) {
        historyAccessLock.use {
            val context = checkNotNull(contexts[uuid]) { "no context for history id: $uuid" }
            context.mergeSemaphore = false
        }
    }

    // --------------------------------------------------------------------------------
    // Initial load
    // --------------------------------------------------------------------------------

    fun loadHistory(path: Path): AioHistoryRuntimeInfo? {

        val metaPath = Path(path, META_NAME).absolute()

        val meta = try {
            AioHistoryMetadata.decodeFromJson(metaPath.read())
        } catch (e: Exception) {
            logger.warning("unable to load history meta file from: $metaPath", e)
            return null
        }

        val store = try {
            val uuid = UUID<TemporalRecordStore>(path.name.substringAfter('.'))
            TemporalRecordStore(uuid, path, meta.signature, 16 * 1024 * 1024)
        } catch (e: Exception) {
            logger.warning("unable to load history index file from: $path", e)
            return null
        }

        return AioHistoryRuntimeInfo(meta, store)
    }

    // --------------------------------------------------------------------------------
    // Management (create, delete)
    // --------------------------------------------------------------------------------

    fun create(uuid: AioHistoryId, signature: String): AioHistoryRuntimeInfo {

        val path = store.pathFor(uuid).ensure()

        val meta = AioHistoryMetadata(uuid, signature, 0, Instant.DISTANT_PAST, Instant.DISTANT_PAST, Instant.DISTANT_PAST)
        val store = TemporalRecordStore(uuid.cast(), path, signature, CHUNK_SIZE)

        val runtimeInfo = AioHistoryRuntimeInfo(meta, store)
        val accessContext = AioHistoryAccessContext(uuid)

        Path(path, META_NAME).write(meta.encodeToJsonByteArray(), useTemporaryFile = true)

        // at this point we cannot have merge running as this is a brand-new history

        historyAccessLock.use {
            contexts[uuid] = accessContext
            histories[uuid] = runtimeInfo
        }

        logger.info { "created history for $uuid with signature $signature" }

        return runtimeInfo
    }

    @OptIn(DangerousApi::class) // as tha path contains the uuid it is quite unique and won't make any trouble
    fun delete(uuid: AioHistoryId) {
        val path = store.pathFor(uuid)
        if (path.exists()) path.deleteRecursively()
    }

    // --------------------------------------------------------------------------------
    // Add
    // --------------------------------------------------------------------------------

    fun append(uuid: AioHistoryId, timestamp: Instant, signature: String, record: ByteArray) {
        addQueue.enqueue(AioHistoryAddQueueEntry(uuid, timestamp, signature, record).encodeToProtoByteArray())
    }

    suspend fun processAddQueue() {
        var addQueueEntry = addQueue.peekOrNull()

        while (addQueueEntry != null) {

            val safeAddQueueEntry = addQueueEntry
            val entry = AioHistoryAddQueueEntry.decodeFromProto(safeAddQueueEntry)

            var runtimeInfo =
                historyAccessLock.use { histories[entry.historyUuid] }
                    ?: create(entry.historyUuid, entry.signature)

            when {

                runtimeInfo.store.lastTimeStamp?.let { it >= entry.timestamp } == true -> {
                    outOfBoundQueue.enqueue(safeAddQueueEntry)
                    logger.warning("history out of bound: ${entry.historyUuid}")
                }

                else -> {
                    try {
                        runOperation(entry.historyUuid) {
                            runtimeInfo.store.append(entry.timestamp, entry.record)
                            updateMetadata(runtimeInfo, entry.timestamp)
                        }
                    } catch (e: Exception) {
                        errorQueue.enqueue(safeAddQueueEntry)
                        logger.warning("couldn't append history entry", e)
                    }
                }
            }

            addQueue.dequeue()
            addQueueEntry = addQueue.peekOrNull()
        }

        delay(5000)
    }

    fun updateMetadata(runtimeInfo: AioHistoryRuntimeInfo, timestamp: Instant) {

        runtimeInfo.metadata.also {
            if (it.firstTimestamp == Instant.DISTANT_PAST) {
                it.firstTimestamp = timestamp
            }
            it.lastTimestamp = timestamp
            it.lastUpdate = now()
            it.recordCount += 1
        }

        val path = store.pathFor(runtimeInfo.metadata.uuid)

        Path(path, META_NAME)
            .write(
                runtimeInfo.metadata.encodeToJsonByteArray(),
                overwrite = true,
                useTemporaryFile = true
            )
    }

    // --------------------------------------------------------------------------------
    // Merge
    // --------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------
    // Query
    // --------------------------------------------------------------------------------

    @JsName("historiesFun")
    fun histories(): List<AioHistoryMetadata> {
        historyAccessLock.use { return histories.values.map { it.metadata } }
    }

    suspend fun query(query: AioHistoryQuery): List<ByteArray> =
        runOperation(query.uuid) {
            histories[query.uuid]?.store?.query(query.from, query.to) ?: emptyList()
        }

}
