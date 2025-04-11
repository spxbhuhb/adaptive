package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
class AdaControllerData<T: AioDeviceSpec>(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val controllerId : AvValueId?,
    val data : T
) : AioDriverAnnouncement()