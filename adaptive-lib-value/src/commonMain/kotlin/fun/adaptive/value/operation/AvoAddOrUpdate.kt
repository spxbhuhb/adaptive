package `fun`.adaptive.value.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValue

@Adat
class AvoAddOrUpdate(
    val value: AvValue
) : AvValueOperation()