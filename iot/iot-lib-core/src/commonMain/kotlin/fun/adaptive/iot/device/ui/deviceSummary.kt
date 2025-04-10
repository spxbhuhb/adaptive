package `fun`.adaptive.iot.device.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun deviceSummary(
    device: AvItem<AioDeviceSpec>?,
    theme: AioTheme = AioTheme.DEFAULT
) {

    grid {
        theme.deviceSummary

        if (device == null) {
            box { maxSize .. backgrounds.lightOverlay }
        } else {
            text(device.friendlyId) .. normalFont
            text(device.name)
            timestamp(device.timestamp)
            status(device.status) .. alignSelf.endCenter
        }
    }

}