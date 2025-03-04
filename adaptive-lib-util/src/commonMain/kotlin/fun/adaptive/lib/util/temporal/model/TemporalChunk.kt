package `fun`.adaptive.lib.util.temporal.model

import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

class TemporalChunk(
    val uuid: UUID<TemporalChunk>,
    val firstTimestamp: Instant,
    val lastTimestamp: Instant
)