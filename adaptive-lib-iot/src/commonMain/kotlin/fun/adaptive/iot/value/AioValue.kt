package `fun`.adaptive.iot.value

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.item.AioStatus
import kotlinx.datetime.Instant

@Adat
class AioValue(
    val uuid: AioValueId,
    val timestamp: Instant,
    val status: AioStatus,
    val value: Any?
)