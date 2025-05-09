package `fun`.adaptive.iot.point

import `fun`.adaptive.value.AvValue

val AvValue<AioPointSpec>.isSimulated
    get() = markers.contains(PointMarkers.SIM_POINT)

val AvValue<AioPointSpec>.isComputed
    get() = markers.contains(PointMarkers.COMPUTED_POINT)