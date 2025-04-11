package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
class AdaCurVal<T>(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val point : AvValueId,
    val value : T
) : AioDriverAnnouncement()