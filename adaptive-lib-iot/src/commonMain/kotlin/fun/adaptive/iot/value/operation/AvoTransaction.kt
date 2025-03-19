package `fun`.adaptive.iot.value.operation

import `fun`.adaptive.adat.Adat

@Adat
class AvoTransaction(
    val operations : List<AioValueOperation>
) : AioValueOperation()