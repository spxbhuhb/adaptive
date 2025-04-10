package `fun`.adaptive.lib.util.temporal

import `fun`.adaptive.lib.util.temporal.model.TemporalChunk
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChunksForTest {

    private fun uuid(): UUID<TemporalChunk> = UUID()

    @Test
    @JsName("returnsEmptyListWhenStartIsBeforeFirstEntryTimestamp")
    fun `returns empty list when lastTimeStamp is null`() {
        val start = Instant.parse("2024-03-01T10:00:00Z")
        val end = Instant.parse("2024-03-01T12:00:00Z")
        val entries = listOf<TemporalIndexEntry>()

        val result = chunksFor(start, end, null, entries)
        assertTrue(result.isEmpty())
    }

    @Test
    @JsName("returnsEmptyListWhenEndIsBeforeFirstEntryTimestamp")
    fun `returns empty list when end is before first entry timestamp`() {
        val start = Instant.parse("2024-03-01T06:00:00Z")
        val end = Instant.parse("2024-03-01T07:00:00Z")
        val entries = listOf(
            TemporalIndexEntry(Instant.parse("2024-03-01T08:30:00Z"), uuid(), 0)
        )

        val result = chunksFor(start, end, entries.last().timestamp, entries)
        assertTrue(result.isEmpty())
    }

    @Test
    @JsName("returnsEmptyListWhenEndIsBeforeFirstEntryTimestamp2")
    fun `returns empty list when entries list is empty`() {
        val start = Instant.parse("2024-03-01T06:00:00Z")
        val end = Instant.parse("2024-03-01T07:00:00Z")
        val entries = emptyList<TemporalIndexEntry>()

        val result = chunksFor(start, end, null, entries)
        assertTrue(result.isEmpty())
    }

    @Test
    @JsName("returnsEmptyListWhenEndIsBeforeFirstEntryTimestamp3")
    fun `returns expected chunk UUIDs when timestamps match the range`() {
        val chunk1 = uuid()
        val chunk2 = uuid()
        val chunk3 = uuid()

        val start = Instant.parse("2024-03-01T08:00:00Z")
        val end = Instant.parse("2024-03-01T11:00:00Z")
        val entries = listOf(
            TemporalIndexEntry(Instant.parse("2024-03-01T07:00:00Z"), chunk1, 0),
            TemporalIndexEntry(Instant.parse("2024-03-01T08:30:00Z"), chunk1, 10),
            TemporalIndexEntry(Instant.parse("2024-03-01T09:30:00Z"), chunk2, 20),
            TemporalIndexEntry(Instant.parse("2024-03-01T10:30:00Z"), chunk3, 30)
        )

        val result = chunksFor(start, end, entries.last().timestamp, entries)
        assertEquals(listOf(chunk1, chunk2, chunk3), result)
    }

    @Test
    @JsName("doesNotIncludeChunksOutsideTheRangeInTheResult")
    fun `does not include duplicate chunks in the result`() {
        val chunk1 = uuid()
        val chunk2 = uuid()

        val start = Instant.parse("2024-03-01T08:00:00Z")
        val end = Instant.parse("2024-03-01T11:00:00Z")
        val entries = listOf(
            TemporalIndexEntry(Instant.parse("2024-03-01T08:30:00Z"), chunk1, 10),
            TemporalIndexEntry(Instant.parse("2024-03-01T09:00:00Z"), chunk1, 15),
            TemporalIndexEntry(Instant.parse("2024-03-01T09:30:00Z"), chunk2, 20),
            TemporalIndexEntry(Instant.parse("2024-03-01T10:30:00Z"), chunk2, 30)
        )

        val result = chunksFor(start, end, entries.last().timestamp, entries)
        assertEquals(listOf(chunk1, chunk2), result)
    }
}