package `fun`.adaptive.iot.point

import `fun`.adaptive.value.item.AvItem

val AvItem<AioPointSpec>.isSimulated
    get() = markers.contains(PointMarkers.SIM_POINT)

val AvItem<AioPointSpec>.isComputed
    get() = markers.contains(PointMarkers.COMPUTED_POINT)