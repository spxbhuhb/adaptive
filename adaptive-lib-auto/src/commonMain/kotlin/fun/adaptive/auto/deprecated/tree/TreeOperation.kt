package `fun`.adaptive.auto.deprecated.tree

import `fun`.adaptive.auto.LamportTimestamp

class TreeOperation(
    val id: NodeId,
    val key: NodeId,
    val value: Value?,
    val timestamp: LamportTimestamp
)