package `fun`.adaptive.iot.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.account_tree
import `fun`.adaptive.adaptive_lib_iot.generated.resources.apartment
import `fun`.adaptive.adaptive_lib_iot.generated.resources.crop_5_4
import `fun`.adaptive.adaptive_lib_iot.generated.resources.database
import `fun`.adaptive.adaptive_lib_iot.generated.resources.host
import `fun`.adaptive.adaptive_lib_iot.generated.resources.meeting_room
import `fun`.adaptive.adaptive_lib_iot.generated.resources.memory
import `fun`.adaptive.adaptive_lib_iot.generated.resources.responsive_layout
import `fun`.adaptive.adaptive_lib_iot.generated.resources.stacks
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.ws.DeviceMarkers
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.builtin.empty

val iconCache: MutableMap<String, GraphicsResourceSet> = mutableMapOf(
    // space
    SpaceMarkers.SITE to Graphics.responsive_layout,
    SpaceMarkers.BUILDING to Graphics.apartment,
    SpaceMarkers.FLOOR to Graphics.stacks,
    SpaceMarkers.ROOM to Graphics.meeting_room,
    SpaceMarkers.AREA to Graphics.crop_5_4,

    // infrastructure
    DeviceMarkers.HOST to Graphics.host,
    DeviceMarkers.NETWORK to Graphics.account_tree,
    DeviceMarkers.DEVICE to Graphics.memory,
    DeviceMarkers.POINT to Graphics.database
)

fun iconFor(item: AioItem): GraphicsResourceSet {

    for (marker in item.markers.keys) {
        val icon = iconCache[marker]
        if (icon != null) return icon
    }

    return Graphics.empty
}