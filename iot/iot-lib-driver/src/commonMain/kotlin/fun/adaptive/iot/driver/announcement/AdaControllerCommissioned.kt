package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem

@Adat
class AdaControllerCommissioned<CT : AioDeviceSpec>(
    override val uuid: UUID<AioDriverAnnouncement>,
    override val networkId: AvValueId,
    val item: AvItem<CT>,
) : AioDriverAnnouncement()