package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.Instant

@Adat
class AioHistoryAddQueueEntry(
    val historyUuid: AioHistoryId,
    val timestamp: Instant,
    val signature: String,
    val record: ByteArray
)