package `fun`.adaptive.iot.point.sim

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.value.item.AvMarker

@Adat
class AioSimPointSpec(
    override val notes: String = "",
) : AioPointSpec()