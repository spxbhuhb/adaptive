package `fun`.adaptive.value.operation

import `fun`.adaptive.adat.Adat

@Adat
class AvoTransaction(
    val operations: List<AvValueOperation>
) : AvValueOperation()