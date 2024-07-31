package hu.simplexion.adaptive.auto.state

import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp

class StateOperation(
    val fragmentId: ItemId,
    val propertyIndex: Int,
    val propertyValue: Any?,
    val timestamp: LamportTimestamp
)