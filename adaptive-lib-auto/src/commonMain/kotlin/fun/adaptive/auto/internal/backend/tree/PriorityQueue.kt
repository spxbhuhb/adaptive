package `fun`.adaptive.auto.internal.backend.tree

/**
 * Note: This priority queue implementation is inefficient. It should
 * probably be implemented using a heap instead. This only matters when
 * there area large numbers of edges on nodes involved in cycles.
 */
class PriorityQueue {
    val items = mutableListOf<Edge>()

    fun push(item: Edge) {
        items += item
        items.sort()
    }

    fun push(items: MutableList<Edge>?) {
        if (items == null) return
        this.items.addAll(items)
        this.items.sort()
    }

    fun pop(): Edge? {
        return this.items.removeLastOrNull()
    }
}