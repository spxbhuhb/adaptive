package `fun`.adaptive.iot.device.ui

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.generated.resources.computer
import `fun`.adaptive.iot.generated.resources.controller
import `fun`.adaptive.iot.generated.resources.device
import `fun`.adaptive.iot.generated.resources.network
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.value.AvValue

val AvValue<AioDeviceSpec>.localizedDeviceType: String
    get() = when (this.type.substringAfterLast(':')) {
        DeviceMarkers.CONTROLLER -> Strings.controller
        DeviceMarkers.NETWORK -> Strings.network
        DeviceMarkers.COMPUTER -> Strings.computer
        else -> Strings.device
    }