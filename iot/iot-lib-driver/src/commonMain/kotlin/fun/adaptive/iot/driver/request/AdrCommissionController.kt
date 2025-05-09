package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue

@Adat
class AdrCommissionController<ST : AioDeviceSpec>(
    override val uuid: UUID<AioDriverRequest>,
    override val networkId: AvValueId,
    val item: AvValue<ST>
) : AioDriverRequest()