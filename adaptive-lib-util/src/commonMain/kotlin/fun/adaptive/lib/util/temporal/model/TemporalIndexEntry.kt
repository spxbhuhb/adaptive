package `fun`.adaptive.lib.util.temporal.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class TemporalIndexEntry(
    val timestamp: Instant,
    val chunk: UUID<TemporalChunk>,
    val offset: Long
)