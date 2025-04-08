package `fun`.adaptive.value.local

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvMarker

@Adat
class AvMarkedValueId(
    val marker: AvMarker,
    val valueId : AvValueId
)