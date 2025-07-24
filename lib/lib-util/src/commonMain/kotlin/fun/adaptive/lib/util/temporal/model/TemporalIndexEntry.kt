package `fun`.adaptive.lib.util.temporal.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.lib.util.temporal.TemporalChunkId
import kotlin.time.Instant

@Adat
class TemporalIndexEntry(
    val timestamp: Instant,
    val chunk: TemporalChunkId,
    val offset: Long
)