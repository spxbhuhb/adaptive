package `fun`.adaptive.lib.util.temporal.model

import `fun`.adaptive.lib.util.temporal.TemporalChunkId
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

class TemporalChunk(
    val uuid: TemporalChunkId,
    val firstTimestamp: Instant,
    val lastTimestamp: Instant
)