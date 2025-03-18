package `fun`.adaptive.iot.ws

import `fun`.adaptive.adaptive_lib_iot.generated.resources.account_tree
import `fun`.adaptive.adaptive_lib_iot.generated.resources.apartment
import `fun`.adaptive.adaptive_lib_iot.generated.resources.crop_5_4
import `fun`.adaptive.adaptive_lib_iot.generated.resources.database
import `fun`.adaptive.adaptive_lib_iot.generated.resources.host
import `fun`.adaptive.adaptive_lib_iot.generated.resources.meeting_room
import `fun`.adaptive.adaptive_lib_iot.generated.resources.memory
import `fun`.adaptive.adaptive_lib_iot.generated.resources.stacks
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

val iconCache: MutableMap<String, GraphicsResourceSet> = mutableMapOf(
    // space
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