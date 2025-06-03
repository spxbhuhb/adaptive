package `fun`.adaptive.value.client

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.model.AvRefLabels
import `fun`.adaptive.value.model.AvTreeDef
import kotlin.reflect.KClass

/**
 * A subscriber that builds a tree from the received values.
 *
 * Observable, the observed value is the list of root nodes (each node is type of [TREE_ITEM]).
 *
 * Observers are notified when the structure of the tree changes.
 *
 * If [TREE_ITEM] is a [SelfObservable] its listeners are notified if the item changes.
 *
 * @param  SPEC  The [spec](def://) values of this tree use.
 * @param  TREE_ITEM  The output tree item type. Usually not a value, a UI tree item, for example.
 */
abstract class AvTreeSubscriber<SPEC : Any, TREE_ITEM>(
    subscribeFun: AvSubscribeFun,
    backend: BackendAdapter,
    val parentRefLabel: AvMarker,
    val specClass: KClass<SPEC>,
) : AvValueSubscriber<List<TREE_ITEM>>(subscribeFun, backend) {

    constructor(
        backend: BackendAdapter,
        parentRefLabel: AvMarker,
        specClass: KClass<SPEC>,
        vararg conditions: AvSubscribeCondition
    ) : this(
        { service, id -> conditions.toList().also { service.subscribe(it) } }, backend, parentRefLabel, specClass
    )

    constructor(
        backend: BackendAdapter,
        specClass: KClass<SPEC>,
        def: AvTreeDef
    ) : this(
        backend,
        def.parentRefLabel,
        specClass,
        avByMarker(def.nodeMarker), avByMarker(def.childListMarker)
    )

    /**
     * List of the top-most tree items. The items should have a list
     * of their own children, set by [updateChildren].
     */
    override var value: List<TREE_ITEM>
        get() = tops.mapNotNull { nodeMap[it.key]?.treeItem }
        set(_) = unsupported()

    private var tops = mutableMapOf<AvValueId, AvValue<SPEC>>()

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

    abstract fun updateTreeItemParent(treeItem: TREE_ITEM, parentItem: TREE_ITEM?)

    abstract fun updateTreeItemData(item: AvValue<SPEC>, treeItem: TREE_ITEM)

    abstract fun updateChildren(treeItem: TREE_ITEM, children: List<TREE_ITEM>)

    operator fun get(valueId: AvValueId) = nodeMap[valueId]?.value

    /**
     * The list that contains [childId]. This is the value of the
     * child reference list or the top value id list if [childId] is a top
     * node.
     */
    fun getSiblingIds(childId: AvValueId): List<AvValueId> {
        val value = nodeMap[childId]?.value ?: return emptyList()
        val parentId = value.refIdOrNull(parentRefLabel)
        if (parentId == null) return tops.keys.toList()
        val parent = nodeMap[parentId] ?: return emptyList()
        return parent.childIds ?: emptyList()
    }

    /**
     * List of child IDs.
     */
    fun getChildrenIds(parentId: AvValueId): List<AvValueId> {
        return nodeMap[parentId]?.childIds ?: return emptyList()
    }

    override fun onCommit() {
        refresh()

        if (topRefresh) {
            notifyListeners()
        }

        // TODO think about the necessity of child refresh in AvTreeSubscriber
        // I commented this out as the usual TreeItem is self-observable and
        // it when `children` changes it will trigger notification by itself

//        for (parent in childRefresh) {
//            // This might trigger a second update if topRefresh is true, but I don't
//            // see any way around it. It might be that this is not necessary if the
//            // child is observable.
//            parent.treeItem.let { if (it is SelfObservable<*>) it.notifyListeners() }
//        }

        topRefresh = false
        childRefresh.clear()
    }

    override fun process(value: AvValue<*>) {
        if (value.spec is AvRefListSpec) {
            processChildList(value, value.spec)
        } else {
            processNode(value.checkSpec(specClass))
        }
    }

    private fun processNode(value: AvValue<SPEC>) {
        val node = nodeMap.getOrPut(value.uuid) { Node() }

        node.value = value

        val parentId = value.refIdOrNull(parentRefLabel)
        if (parentId != null) {
            tops.remove(value.uuid)
        } else {
            tops.put(value.uuid, value)
        }

        val treeItem = node.treeItem

        if (treeItem == null) {
            val parentNode = parentId?.let { nodeMap[it] }

            node.treeItem = newTreeItem(value, parentNode)

            if (parentNode != null) {
                childRefresh += parentNode
            } else {
                topRefresh = true
            }
        } else {
            updateTreeItemData(value, treeItem)
        }

    }

    private fun processChildList(list: AvValue<*>, spec: AvRefListSpec) {
        val node = nodeMap.getOrPut(list.refId(AvRefLabels.REF_LIST_OWNER)) { Node() }
        node.childIds = spec.refs
        childRefresh += node
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
        childRefresh.forEach { parent ->
            parent.treeItem?.let { parentTreeItem ->

                val children = parent.childIds?.mapNotNull { childId ->
                    // this covers the case when a child arrives before the parent and therefore
                    // the parent is not set in the child tree node
                    nodeMap[childId]?.treeItem?.also { updateTreeItemParent(it, parentTreeItem) }
                } ?: emptyList()

                updateChildren(parentTreeItem, children)
            }
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

        var current: AvValue<*>? = value
        while (current != null) {
            names.add(current.nameLike)
            current = current.refIdOrNull(parentRefLabel)?.let { this[it] }
        }

        return names.reversed()
    }
}