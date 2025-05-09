package `fun`.adaptive.value

import `fun`.adaptive.adat.Adat

@Adat
class AvSubscribeCondition(
    val valueId: AvValueId? = null,
    val marker: AvMarker? = null,
    val itemOnly : Boolean = false
)