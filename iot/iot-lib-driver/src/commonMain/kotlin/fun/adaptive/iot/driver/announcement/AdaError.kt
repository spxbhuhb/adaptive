package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.resource.StringResourceKey
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
class AdaError<T>(
    override val uuid: UUID<AioDriverAnnouncement>,
    override val networkId: AvValueId,
    val subject : AvValueId,
    val messageKey : StringResourceKey,
    val attachment : T
) : AioDriverAnnouncement()