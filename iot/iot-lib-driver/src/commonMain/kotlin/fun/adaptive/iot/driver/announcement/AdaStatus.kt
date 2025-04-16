package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Instant

@Adat
class AdaStatus(
    override val uuid: UUID<AioDriverAnnouncement>,
    override val networkId: AvValueId,
    val owner : AvValueId,
    val status : AvStatus
) : AioDriverAnnouncement()