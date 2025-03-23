package `fun`.adaptive.iot.device.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun controllerSummary(device : AvItem<AioDeviceSpec>?) {

    row {
        gap { 16.dp } .. height { 32.dp }

        if (device == null) {
            box { maxSize .. backgrounds.lightOverlay }
        } else {
            text(device.friendlyId)
            text(device.name)
            status(device.status)
            timestamp(device.timestamp)
        }
    }

}