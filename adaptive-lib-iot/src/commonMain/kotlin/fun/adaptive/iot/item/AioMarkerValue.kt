package `fun`.adaptive.iot.item

import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId

abstract class AioMarkerValue : AioValue() {
    abstract val owner : AioValueId
    abstract val markerName: String

    override val name: String
        get() = markerName

    override val type: String
        get() = "aio:marker:$markerName"

}