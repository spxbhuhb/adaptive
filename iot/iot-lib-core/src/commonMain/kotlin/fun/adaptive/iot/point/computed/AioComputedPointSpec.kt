package `fun`.adaptive.iot.point.computed

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.conversion.CurValConversion
import `fun`.adaptive.value.item.AvMarker

@Adat
class AioComputedPointSpec(
    override val displayAddress: String = "",
    override val notes: String = "",
    override val conversion: CurValConversion? = null,
    val dependencyMarker: AvMarker
) : AioPointSpec()