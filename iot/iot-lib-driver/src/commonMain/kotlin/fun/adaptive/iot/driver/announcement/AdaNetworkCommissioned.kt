package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
class AdaNetworkCommissioned<NT : AioDeviceSpec>(
    override val uuid: UUID<AioDriverAnnouncement>,
    override val networkId: AvValueId,
    val item: AvItem<NT>,
) : AioDriverAnnouncement()