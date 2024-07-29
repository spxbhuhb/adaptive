package hu.simplexion.adaptive.crdt.state

import hu.simplexion.adaptive.crdt.FragmentId
import hu.simplexion.adaptive.crdt.LamportTimestamp

class StateOperation(
    val fragmentId: FragmentId,
    val propertyIndex: Int,
    val propertyValue: Any?,
    val timestamp: LamportTimestamp
)