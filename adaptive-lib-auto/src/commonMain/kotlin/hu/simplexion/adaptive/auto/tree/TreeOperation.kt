package hu.simplexion.adaptive.auto.tree

import hu.simplexion.adaptive.auto.LamportTimestamp

class TreeOperation(
    val id: NodeId,
    val key: NodeId,
    val value: Value?,
    val timestamp: LamportTimestamp
)