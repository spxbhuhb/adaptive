package `fun`.adaptive.lib.util.temporal.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.lib.util.temporal.TemporalChunkId
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class TemporalIndexEntry(
    val timestamp: Instant,
    val chunk: TemporalChunkId,
    val offset: Long
)