package `fun`.adaptive.value

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvMarker

@Adat
class AvSubscribeCondition(
    val valueId: AvValueId? = null,
    val marker: AvMarker? = null,
    val itemOnly : Boolean = false
)