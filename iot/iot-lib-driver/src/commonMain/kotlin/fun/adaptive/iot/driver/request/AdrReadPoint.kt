package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
class AdrReadPoint(
    override val uuid: UUID<AioDriverRequest>,
    override val networkId: AvValueId,
    val pointId : AvValueId
) : AioDriverRequest()