package `fun`.adaptive.lib.util.temporal

import `fun`.adaptive.lib.util.temporal.model.TemporalChunk
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant


fun chunksFor(start: Instant, end: Instant, lastTimeStamp: Instant?, entries: List<TemporalIndexEntry>): List<UUID<TemporalChunk>> {

    // if the query start is after the last known timestamp, we surely have no records
    val currentLast = lastTimeStamp
    if (currentLast == null || start > currentLast) return emptyList()

    // if the query end is before the first known timestamp, we surely have no records
    if (end < entries.first().timestamp) return emptyList()

    // If we have no chunks at all, we have no records for sure
    if (entries.isEmpty()) return emptyList()

    // The request is silly, just return with nothing
    if (start > end) return emptyList()

    // At this point we surely have some records
    var index = 0
    val endIndex = entries.size

    // find the first entry with a timestamp larger than start
    // this is not the one we want to include but the one after

    while (index < endIndex && entries[index].timestamp < start) {
        index ++
    }

    if (index > 0) index -- // step back one entry as start is before the one index points at

    val result = mutableListOf<UUID<TemporalChunk>>()

    while (index < endIndex && entries[index].timestamp <= end) {
        val id = entries[index].chunk
        if (result.lastOrNull() != id) {
            result += id
        }
        index ++
    }

    return result
}