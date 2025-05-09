package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue

@Adat
class AdrCommissionPoint<ST : AioPointSpec>(
    override val uuid: UUID<AioDriverRequest>,
    override val networkId: AvValueId,
    val item: AvValue<ST>
) : AioDriverRequest()