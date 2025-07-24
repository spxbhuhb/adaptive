package `fun`.adaptive.lib.util.temporal

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.persistence.clearedTestPath
import `fun`.adaptive.persistence.list
import `fun`.adaptive.persistence.read
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.dump
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import `fun`.adaptive.wireformat.protobuf.dumpProto
import kotlin.time.Instant
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.minutes

@Adat
class SomeRecord(
    val timestamp: Instant,
    val data: Int
)

class TemporalRecordStoreTest {

    @Test
    fun basic() {
        val testDir = clearedTestPath()
        val store = TemporalRecordStore(uuid4(), testDir, "", 100)

        val timestamp = Instant.parse("2024-01-01T00:00:00Z")
        val someRecord = SomeRecord(timestamp, 12)

        store.append(timestamp, someRecord.encodeToProtoByteArray())

        val actual = mutableListOf<SomeRecord>()

        store
            .query(timestamp.minus(1.minutes), timestamp.plus(1.minutes))
            .decodeChunks(actual)

        assertEquals(1, actual.size)
        assertEquals(listOf(someRecord), actual)
    }

    @Test
    fun appendMultipleRecords() {
        val testDir = clearedTestPath()
        val store = TemporalRecordStore(uuid4(), testDir, "", 100)

        val timestamp1 = Instant.parse("2024-01-01T00:00:00Z")
        val timestamp2 = Instant.parse("2024-01-01T00:05:00Z")
        val record1 = SomeRecord(timestamp1, 10)
        val record2 = SomeRecord(timestamp2, 20)

        store.append(timestamp1, record1.encodeToProtoByteArray())
        store.append(timestamp2, record2.encodeToProtoByteArray())

        val actual = mutableListOf<SomeRecord>()
        store.query(timestamp1.minus(1.minutes), timestamp2.plus(1.minutes)).decodeChunks(actual)

        assertEquals(2, actual.size)
        assertEquals(listOf(record1, record2), actual)
    }

    @Test
    fun queryEmptyStore() {
        val testDir = clearedTestPath()
        val store = TemporalRecordStore(uuid4(), testDir, "", 100)

        val timestamp = Instant.parse("2024-01-01T00:00:00Z")
        val actual = mutableListOf<SomeRecord>()

        store.query(timestamp.minus(1.minutes), timestamp.plus(1.minutes)).decodeChunks(actual)

        assertEquals(0, actual.size)
    }

    @Test
    fun appendDuplicateTimestamps() {
        val testDir = clearedTestPath()
        val store = TemporalRecordStore(uuid4(), testDir, "", 100)

        val timestamp = Instant.parse("2024-01-01T00:00:00Z")
        val record1 = SomeRecord(timestamp, 10)
        val record2 = SomeRecord(timestamp, 20)

        store.append(timestamp, record1.encodeToProtoByteArray())
        assertFailsWith<IllegalArgumentException> {
            store.append(timestamp, record2.encodeToProtoByteArray())
        }
    }

    @Test
    fun appendAndQueryLargeDataSet() {
        val testDir = clearedTestPath()
        val store = TemporalRecordStore(uuid4(), testDir, "", 100)

        val baseTimestamp = Instant.parse("2024-01-01T00:00:00Z")
        val records = mutableListOf<SomeRecord>()

        for (i in 0 until 100) {
            val timestamp = baseTimestamp.plus(i.minutes)
            val record = SomeRecord(timestamp, i)
            store.append(timestamp, record.encodeToProtoByteArray())
            records.add(record)
        }

        val actualAll = mutableListOf<SomeRecord>()
        store.query(baseTimestamp.minus(1.minutes), baseTimestamp.plus(100.minutes)).decodeChunks(actualAll)
        assertEquals(100, actualAll.size)
        assertEquals(records, actualAll)

        val actualPartial = mutableListOf<SomeRecord>()
        store.query(baseTimestamp.plus(30.minutes), baseTimestamp.plus(60.minutes)).decodeChunks(actualPartial)

        // this check is tricky because we will actually get a larger set than the queried one
        // in this specific case I manually checked the returned range, and it seems to be OK

        assertEquals(36, actualPartial.size)
        assertEquals(records.subList(28, 64).joinToString("\n"), actualPartial.joinToString("\n"))
    }

    fun List<ByteArray>.decodeChunks(out: MutableList<SomeRecord>) {
        forEach {
            it.decodeChunk(out)
        }
    }

    fun ByteArray.decodeChunk(out: MutableList<SomeRecord>) {
        val decoder = ProtoWireFormatDecoder(this)

        for (record in decoder.records) {
            out += record.decoder().rawInstance(record, SomeRecord)
        }
    }

    fun dumpChunks(path: Path) {
        path.list().forEach {
            val name = it.name
            if (name.startsWith(".") || name.startsWith("index")) return@forEach

            dump { "========  $name  ========" }
            dump { it.read().dumpProto() }
        }
    }
}