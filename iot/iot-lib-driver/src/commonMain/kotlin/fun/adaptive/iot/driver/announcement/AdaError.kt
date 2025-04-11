package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.resource.StringResourceKey
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
class AdaError<T>(
    override val timestamp: Instant,
    override val networkId: AvValueId,
    val subject : AvValueId,
    val messageKey : StringResourceKey,
    val attachment : T
) : AioDriverAnnouncement()