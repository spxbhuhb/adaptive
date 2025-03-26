package `fun`.adaptive.iot.point.computed

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.value.item.AvMarker

@Adat
class AioComputedPointSpec(
    override val notes: String = "",
    val dependencyMarker : AvMarker
) : AioPointSpec()