package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

abstract class AioDriverAnnouncement : AdatClass {
    abstract val uuid: UUID<AioDriverAnnouncement>
    abstract val networkId : AvValueId
}