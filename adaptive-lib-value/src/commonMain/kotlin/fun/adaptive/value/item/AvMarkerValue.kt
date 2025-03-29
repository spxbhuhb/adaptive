package `fun`.adaptive.value.item

import `fun`.adaptive.value.AvValue

abstract class AvMarkerValue : AvValue() {

    abstract val markerName: String

    override val name: String
        get() = markerName

    override val type: String
        get() = "av:marker:$markerName"

}