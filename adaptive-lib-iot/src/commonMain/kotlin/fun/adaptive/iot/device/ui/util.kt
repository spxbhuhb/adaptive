package `fun`.adaptive.iot.device.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.resource.string.Strings

val AvItem<AioDeviceSpec>.localizedDeviceType: String
    get() = when (this.type.substringAfterLast(':')) {
        DeviceMarkers.CONTROLLER -> Strings.controller
        DeviceMarkers.NETWORK -> Strings.network
        DeviceMarkers.COMPUTER -> Strings.computer
        else -> Strings.device
    }