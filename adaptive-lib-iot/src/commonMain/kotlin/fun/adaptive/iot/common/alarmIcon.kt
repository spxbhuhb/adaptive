package `fun`.adaptive.iot.common

import `fun`.adaptive.adaptive_lib_iot.generated.resources.notifications
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun alarmIcon(flags : Int) {
    val status = AvStatus(flags)

    if (!status.isOk) {
        icon(Graphics.notifications) .. fill(colors.fail) .. svgWidth(24.dp) .. svgHeight(24.dp)
    }
}