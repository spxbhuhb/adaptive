package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Adat
class AdrCommissionController<ST : AioDeviceSpec>(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val item : AvItem<ST>
) : AioDriverRequest()