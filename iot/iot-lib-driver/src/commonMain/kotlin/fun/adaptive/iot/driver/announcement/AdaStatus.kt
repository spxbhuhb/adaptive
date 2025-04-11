package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Instant

@Adat
class AdaStatus(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val owner : AvValueId,
    val status : AvStatus
) : AioDriverAnnouncement()