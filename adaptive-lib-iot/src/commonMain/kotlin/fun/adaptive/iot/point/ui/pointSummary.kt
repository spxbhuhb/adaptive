package `fun`.adaptive.iot.point.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun pointSummary(
    point: AvItem<AioPointSpec>?,
    theme: AioTheme = AioTheme.DEFAULT
) {

    grid {
        theme.pointSummary

        if (point == null) {
            box { maxSize .. backgrounds.lightOverlay }
        } else {
            text(point.name)
            timestamp(point.timestamp)
            status(point.status) .. alignSelf.endCenter
        }
    }

}