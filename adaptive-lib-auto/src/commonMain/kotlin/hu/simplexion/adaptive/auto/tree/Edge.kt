package hu.simplexion.adaptive.auto.tree

class Edge(
    val parent: Node,
    val child: Node,
    val counter: Int
) : Comparable<Edge> {

    override fun compareTo(other: Edge): Int {
        val counterDelta = this.counter - other.counter

        return when {
            counterDelta != 0 -> counterDelta
            this.parent.id < other.parent.id -> - 1
            this.parent.id > other.parent.id -> 1
            this.child.id < other.child.id -> - 1
            this.child.id > other.child.id -> 1
            else -> 0
        }
    }
}