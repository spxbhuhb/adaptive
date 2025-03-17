package `fun`.adaptive.iot.curval

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.model.AioStatus
import `fun`.adaptive.iot.model.AioValueId
import kotlinx.datetime.Instant

@Adat
class CurVal(
    val uuid: AioValueId,
    val timestamp: Instant,
    val status: AioStatus,
    val value: Any?
)