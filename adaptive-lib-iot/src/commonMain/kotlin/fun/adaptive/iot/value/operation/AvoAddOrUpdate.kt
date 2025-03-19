package `fun`.adaptive.iot.value.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.value.AioValue

@Adat
class AvoAddOrUpdate(
    val value : AioValue
) : AioValueOperation()