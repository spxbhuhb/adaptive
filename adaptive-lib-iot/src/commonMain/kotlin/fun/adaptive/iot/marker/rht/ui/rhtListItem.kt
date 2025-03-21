package `fun`.adaptive.iot.marker.rht.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.iot.marker.rht.AmvRelativeHumidityAndTemperature
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.format
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Adaptive
fun rhtListItem(
    item: AvItem
) : AdaptiveFragment {

    val rht = AmvRelativeHumidityAndTemperature(item.uuid, Double.NaN, Double.NaN)

    grid {
        colTemplate(1.fr, 1.fr, 100.dp, 100.dp, 160.dp, 160.dp)

        text(item.friendlyId)
        text(item.name)

        text(rht.temperature.temperatureString())
        text(rht.relativeHumidity.humidityString(0))
        status(rht.status)
        text(rht.timestamp.localizedString())
    }

    return fragment()
}

fun Double.temperatureString(decimals : Int = 1) =
    "${this.format(decimals)} °C"

fun Double.humidityString(decimals : Int = 1) =
    "${this.format(decimals)} %"

fun Instant.localizedString() =
    this
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toString()
        .replace("T", " ")
        .substringBeforeLast('.')

@Adaptive
fun status(status: AvStatus) {
    val border = when {
        status.isOk -> border(colors.success, 2.dp)
        else -> borders.fail
    }
    box {
        alignItems.center .. border
        text("Status")
    }
}