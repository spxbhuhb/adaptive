package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Instant

@Adat
class AdrCommissionPoint<ST : AioPointSpec>(
    override val uuid: UUID<AioDriverRequest>,
    override val networkId: AvValueId,
    val item : AvItem<ST>
) : AioDriverRequest()