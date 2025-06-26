package `fun`.adaptive.value.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId

@Adat
class AvoRemove(
    val valueId : AvValueId
) : AvValueOperation()