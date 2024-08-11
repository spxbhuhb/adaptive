package `fun`.adaptive.auto.deprecated.state

import `fun`.adaptive.auto.ItemId
import `fun`.adaptive.auto.LamportTimestamp

class StateOperation(
    val fragmentId: ItemId,
    val propertyIndex: Int,
    val propertyValue: Any?,
    val timestamp: LamportTimestamp
)