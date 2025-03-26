package `fun`.adaptive.iot.domain.rht.ui

import `fun`.adaptive.iot.generated.resources.monitoring
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.*
import `fun`.adaptive.iot.haystack.PhScienceMarkers
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.ui.AvUiList
import `fun`.adaptive.value.ui.AvUiValue
import `fun`.adaptive.value.ui.iconFor

@Adaptive
fun rhtListItem(
    item: AvItem<*>,
    theme : AioTheme = AioTheme.DEFAULT
) : AdaptiveFragment {

    val points = valueFrom { AvUiList(adapter(), item.uuid, PointMarkers.POINTS) }

    val temperature = points.firstOrNull { PhScienceMarkers.TEMP in it.markers }
    val humidity = points.firstOrNull { PhScienceMarkers.HUMIDITY in it.markers }

    val tempValue = valueFrom { AvUiValue<AvDouble>(adapter(), temperature?.markers[PointMarkers.CUR_VAL]) }
    val humidityValue = valueFrom { AvUiValue<AvDouble>(adapter(), humidity?.markers[PointMarkers.CUR_VAL]) }

    grid {
        theme.itemListItemContainer
        colTemplate(36.dp, 80.dp, 1.fr, 0.5.fr, 0.5.fr, 82.dp, 60.dp, 160.dp, 48.dp)

        box {
            theme.itemListIconContainer
            icon(iconFor(item)) .. theme.itemListIcon
        }
        text(item.friendlyId)
        text(item.name)

        temperature(tempValue?.value)
        relativeHumidity(humidityValue?.value)
        status(item.status)

        box {
            maxSize .. alignItems.center
            alarmIcon(item.status.flags)
        }

        timestamp(item.timestamp) .. textSmall .. alignSelf.center

        box {
            maxSize .. alignItems.center
            actionIcon(Graphics.monitoring)
        }

    }

    return fragment()
}