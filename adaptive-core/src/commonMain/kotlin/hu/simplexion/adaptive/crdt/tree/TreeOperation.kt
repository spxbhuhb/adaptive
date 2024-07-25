package hu.simplexion.adaptive.crdt.tree

import hu.simplexion.adaptive.crdt.LamportTimestamp

class TreeOperation(
    val id: NodeId,
    val key: NodeId,
    val value: Value?,
    val timestamp: LamportTimestamp
)