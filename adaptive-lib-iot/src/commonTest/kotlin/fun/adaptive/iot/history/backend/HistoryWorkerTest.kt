package `fun`.adaptive.iot.history.backend

import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.foundation.query.singleImpl
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.model.AioHistoryId
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.io.files.Path
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds

class HistoryWorkerTest {

    @Test
    fun create() = runTest {

        val (rootPath, _, worker, uuid) = init(clearedTestPath())

        val path = Path(rootPath, "9f", "39", uuid.toString())
        assertTrue(path.exists())

        val info = worker.loadHistory(path)
        assertNotNull(info)

        val metadata = info.metadata

        assertEquals(uuid, metadata.uuid)
        assertEquals(0, metadata.recordCount)
        assertEquals(Instant.DISTANT_PAST, metadata.lastUpdate)
        assertEquals(Instant.DISTANT_PAST, metadata.lastTimestamp)

        val store = info.store

        assertEquals(uuid, store.storeUuid.cast())
        assertEquals(null, store.lastTimeStamp)
    }

    @Test
    fun createAndAppendOne() = runTest {

        val (_, _, worker, uuid) = init(clearedTestPath())
        val timestamp = now().minus(1.seconds)

        worker.append(
            uuid,
            timestamp,
            AioDoubleHistoryRecord.typeSignature(),
            AioDoubleHistoryRecord(timestamp, 12.0, 23).encodeToProtoByteArray()
        )

        waitForReal(7.seconds)

        val histories = worker.histories()
        assertEquals(1, histories.size)

        val history = histories.first()
        assertEquals(uuid, history.uuid)
        assertEquals(1, history.recordCount)
        assertNotEquals(timestamp, history.lastUpdate)
        assertTrue(timestamp < history.lastUpdate && history.lastUpdate <= now())
        assertEquals(timestamp, history.lastTimestamp)
    }

    data class TestContext(
        val rootPath: Path,
        val adapter: BackendAdapter,
        val worker: AioHistoryWorker,
        val uuid: AioHistoryId
    )

    suspend fun init(rootPath: Path): TestContext {

        settings {
            inline("AIO_HISTORY_PATH" to rootPath)
        }

        iotCommon(false)

        val adapter = backend {
            worker { AioHistoryWorker() }
        }

        val worker = adapter.singleImpl<AioHistoryWorker>()

        waitFor(10.seconds) { worker.isInitialized }

        val uuid = AioHistoryId("be39e8fe-5015-4260-9f39-271612bc399f")
        worker.create(uuid, AioDoubleHistoryRecord.typeSignature())

        return TestContext(rootPath, adapter, worker, uuid)
    }

}