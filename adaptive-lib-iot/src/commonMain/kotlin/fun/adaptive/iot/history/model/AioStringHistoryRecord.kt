package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.Instant

@Adat
class AioStringHistoryRecord(
    val timestamp: Instant,
    val value: String,
    val flags: Int
)