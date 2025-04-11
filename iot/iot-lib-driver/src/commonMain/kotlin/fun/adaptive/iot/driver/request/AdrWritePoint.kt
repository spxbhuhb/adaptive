package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
class AdrWritePoint<T>(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val pointId : AvValueId,
    val value : T
) : AioDriverRequest()