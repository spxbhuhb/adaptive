package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue

@Adat
class AdaNetworkCommissioned<NT : AioDeviceSpec>(
    override val uuid: UUID<AioDriverAnnouncement>,
    override val networkId: AvValueId,
    val item: AvValue<NT>,
) : AioDriverAnnouncement()