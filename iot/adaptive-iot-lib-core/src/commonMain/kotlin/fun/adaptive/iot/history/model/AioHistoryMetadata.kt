package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.Instant

@Adat
class AioHistoryMetadata(
    val uuid: AioHistoryId,
    val signature: String,
    var recordCount: Long,
    var lastUpdate: Instant,
    var firstTimestamp: Instant,
    var lastTimestamp: Instant
)