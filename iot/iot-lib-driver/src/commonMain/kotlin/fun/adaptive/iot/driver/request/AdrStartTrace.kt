package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Adat
class AdrStartTrace(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val subjectId : AvValueId,
    val timeLimit : Duration
) : AioDriverRequest()