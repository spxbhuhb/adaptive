package `fun`.adaptive.iot.driver.announcement

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

abstract class AioDriverAnnouncement : AdatClass {
    abstract val timestamp : Instant
    abstract val networkId : AvValueId
}