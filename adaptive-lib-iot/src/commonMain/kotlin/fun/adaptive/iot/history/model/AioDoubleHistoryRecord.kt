package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.Instant

@Adat
class AioDoubleHistoryRecord(
    val timestamp: Instant,
    val value: Double,
    val flags: Int
)