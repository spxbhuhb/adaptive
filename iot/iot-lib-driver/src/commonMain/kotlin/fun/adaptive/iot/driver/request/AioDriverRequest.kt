package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

abstract class AioDriverRequest : AdatClass {
    abstract val timestamp: Instant
    abstract val networkId: AvValueId
}