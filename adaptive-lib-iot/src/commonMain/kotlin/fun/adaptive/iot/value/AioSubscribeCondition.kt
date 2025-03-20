package `fun`.adaptive.iot.value

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.item.AioMarker

@Adat
class AioSubscribeCondition(
    val valueId : AioValueId? = null,
    val marker : AioMarker? = null,
    val itemOnly : Boolean = false
)