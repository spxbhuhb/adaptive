package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue.Companion.withSpec
import kotlin.reflect.KClass

/**
 * A subscriber that builds a tree from the received values.
 */
abstract class AvTreeSubscriber<SPEC : Any, TREE_ITEM>(
    subscribeFun: AvSubscribeFun,
    backend: BackendAdapter,
    val specClass: KClass<SPEC>,
) : AvValueSubscriber<List<TREE_ITEM>>(subscribeFun, backend) {

    abstract val topListMarker: AvMarker
    abstract val childListMarker: AvMarker

    /**
     * List of the top-most tree items. The items should have a list
     * of their own children, set by [updateChildren].
     */
    override var value: List<TREE_ITEM>
        get() = topIds.mapNotNull { nodeMap[it]?.treeItem }
        set(_) = unsupported()

    private var topIds = emptyList<AvValueId>()

    private val nodeMap = mutableMapOf<AvValueId, Node<SPEC, TREE_ITEM>>()

    class Node<SPEC, TREE_ITEM>(
        var value: AvValue<SPEC>? = null,
        var childIds: List<AvValueId>? = null,
        var treeItem: TREE_ITEM? = null
    )

    private var topRefresh = false
    private val childRefresh = mutableSetOf<Node<SPEC, TREE_ITEM>>()

    val size
        get() = nodeMap.size

    abstract fun newTreeItem(item: AvValue<SPEC>, parentNode: Node<SPEC, TREE_ITEM>?): TREE_ITEM

    abstract fun updateTreeItem(item: AvValue<SPEC>, treeItem: TREE_ITEM)

    abstract fun updateChildren(treeItem: TREE_ITEM, children: List<TREE_ITEM>)

    operator fun get(valueId: AvValueId) = nodeMap[valueId]?.value

    /**
     * The sub items list that contains [childId]. This is the value of the
     * sub items marker from the parent node or the top items list if
     * [childId] is a top node.
     */
//    fun getParentSubItems(childId: AvValueId): List<AvValueId> {
//        val aioItem = nodeMap[childId]?.value ?: return emptyList()
//        if (aioItem.parentId == null) return topIds
//        val parent = nodeMap[aioItem.parentId] ?: return emptyList()
//        return parent.childIds ?: emptyList()
//    }

    /**
     * The sub items list of [childId].
     */
    fun getSubItems(childId: AvValueId): List<AvValueId> {
        return nodeMap[childId]?.childIds ?: return emptyList()
    }

    override fun onCommit() {
        refresh()
        childRefresh.clear()
        topRefresh = false
    }

//    override fun process(value: AvValue<*>) {
//        when (value) {
//            is AvValue<*> -> process(value)
//            is AvRefList -> process(value)
//        }
//    }

    private fun process3(item: AvValue<*>) {
//        val node = nodeMap.getOrPut(item.uuid) { Node() }
//
//        val avItem = item.withSpec(specClass)
//        node.value = avItem
//
//        val parentId = item.parentId
//        val treeItem = node.treeItem
//
//        if (treeItem == null) {
//            val parentNode = parentId?.let { nodeMap[it] }
//
//            node.treeItem = newTreeItem(avItem, parentNode)
//
//            if (parentNode != null) {
//                childRefresh += parentNode
//            } else {
//                topRefresh = true
//            }
//        } else {
//           updateTreeItem(avItem, treeItem)
//        }
    }

    private fun process2(list: AvValue<List<AvValueId>>) {
//        when (list.name) {
//            childListMarker -> {
//                val node = nodeMap.getOrPut(list.parentId !!) { Node() }
//                node.childIds = list.spec
//                childRefresh += node
//            }
//
//            topListMarker -> {
//                topIds = list.spec
//                topRefresh = true
//            }
//        }
    }

    /**
     * When refresh is called, all nodes are updated from the
     * incoming transaction. This means that we have the most
     * complete data available at this point:
     *
     * - all [Node] instances are in the [nodeMap]
     * - all possible tree item instances are created
     */
    fun refresh() {
        childRefresh.forEach { node ->
            node.treeItem?.let { ti ->
                updateChildren(ti, node.childIds?.mapNotNull { nodeMap[it]?.treeItem } ?: emptyList())
            }
        }

        if (topRefresh) {
            notifyListeners()
        }
//
//        nodeMap.values.forEach { node ->
//            println("${node.aioItem?.name} ${node.aioItem?.uuid} ${node.childIds}")
//        }
//
//        topIds.mapNotNull { nodeMap[it]?.treeItem }.forEach { ti ->
//            ti.dumpTree().debug()
//        }
    }

    /**
     * Collect names of this item and its parents into a list.
     *
     * First item of the list is the topmost item without a
     * parent.
     */
    fun pathNames(value: AvValue<*>): List<String> {
        val names = mutableListOf<String>()
//
//        var current: AvValue<*>? = value
//        while (current != null) {
//            names.add(current.name)
//            current = current.parentId?.let { this[it] }
//        }
//
        return names.reversed()
    }
}