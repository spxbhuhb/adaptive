package hu.simplexion.adaptive.auto.deprecated.tree

class Node(val id: NodeId) : Comparable<Node> {
    var parent: Node? = null
    val children: MutableList<Node> = mutableListOf()
    val edges = mutableMapOf<NodeId, Value>()

    override fun compareTo(other: Node): Int {
        if (this.id < other.id) return - 1
        if (this.id > other.id) return 1
        return 0
    }
}
