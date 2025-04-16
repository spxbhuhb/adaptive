package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Adat
class AdrCommissionNetwork<ST : AioDeviceSpec>(
    override val uuid: UUID<AioDriverRequest>,
    override val networkId: AvValueId,
    val item: AvItem<ST>,
) : AioDriverRequest()