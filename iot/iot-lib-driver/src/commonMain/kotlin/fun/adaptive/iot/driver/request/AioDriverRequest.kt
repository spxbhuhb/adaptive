package `fun`.adaptive.iot.driver.request

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId

abstract class AioDriverRequest : AdatClass {
    abstract val uuid: UUID<AioDriverRequest>
    abstract val networkId: AvValueId
}