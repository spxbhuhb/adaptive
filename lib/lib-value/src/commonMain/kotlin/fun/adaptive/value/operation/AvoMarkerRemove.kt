package `fun`.adaptive.value.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvValueId

@Adat
class AvoMarkerRemove(
    val valueId: AvValueId,
    val marker: AvMarker
) : AvValueOperation()