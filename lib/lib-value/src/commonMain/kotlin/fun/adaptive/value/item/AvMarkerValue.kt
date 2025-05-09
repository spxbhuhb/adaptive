package `fun`.adaptive.value.item

import `fun`.adaptive.value.AvValue2

abstract class AvMarkerValue : AvValue2() {

    abstract val markerName: String

    override val name: String
        get() = markerName

    override val type: String
        get() = "av:marker:$markerName"

}