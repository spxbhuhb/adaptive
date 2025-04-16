package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
class AdaCurVal<T>(
    override val uuid: UUID<AioDriverAnnouncement>,
    override val networkId: AvValueId,
    val point : AvValueId,
    val value : T
) : AioDriverAnnouncement()