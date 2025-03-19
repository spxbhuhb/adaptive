package `fun`.adaptive.iot.value.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.value.AioValueId

@Adat
class AvoMarkerRemove(
    val valueId: AioValueId,
    val marker: AioMarker
) : AioValueOperation()