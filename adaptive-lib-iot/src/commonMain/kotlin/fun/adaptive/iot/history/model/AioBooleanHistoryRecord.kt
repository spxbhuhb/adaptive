package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.Instant

@Adat
class AioBooleanHistoryRecord(
    val timestamp: Instant,
    val value: Boolean,
    val flags: Int
)