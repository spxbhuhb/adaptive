package `fun`.adaptive.iot.marker.rht.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.common.relativeHumidity
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.temperature
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.marker.rht.AmvRelativeHumidityAndTemperature
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun rhtListItem(
    item: AvItem<*>
) : AdaptiveFragment {

    val rht = AmvRelativeHumidityAndTemperature(item.uuid, Double.NaN, Double.NaN)

    grid {
        colTemplate(1.fr, 1.fr, 100.dp, 100.dp, 160.dp, 160.dp)

        text(item.friendlyId)
        text(item.name)

        temperature(rht.temperature)
        relativeHumidity(rht.relativeHumidity)
        status(rht.status)
        timestamp(rht.timestamp)
    }

    return fragment()
}