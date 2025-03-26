package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.Instant

@Adat
class AioHistoryQuery(
    val uuid: AioHistoryId,
    val from: Instant,
    val to: Instant
)