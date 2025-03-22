package `fun`.adaptive.iot.device.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.iot.device.marker.DeviceMarkers
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.resource.string.Strings

val AvItem.localizedDeviceType: String
    get() = when (this.type.substringAfterLast(':')) {
        DeviceMarkers.POINT -> Strings.point
        DeviceMarkers.CONTROLLER -> Strings.controller
        DeviceMarkers.NETWORK -> Strings.network
        DeviceMarkers.COMPUTER -> Strings.computer
        else -> Strings.device
    }