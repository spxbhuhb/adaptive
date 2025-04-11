package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Adat
class AdrStopTrace(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val subjectId : AvValueId
) : AioDriverRequest()