package `fun`.adaptive.value.ui

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvChannelSubscription
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueApi
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItemIdList
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.value.operation.AvoAdd
import `fun`.adaptive.value.operation.AvoAddOrUpdate
import `fun`.adaptive.value.operation.AvoMarkerRemove
import `fun`.adaptive.value.operation.AvoUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AvUiTree(
    adapter: BackendAdapter,
    transport: ServiceCallTransport,
    val scope: CoroutineScope,
    itemMarker: AvMarker,
    val childListMarker: AvMarker,
    val topListMarker: AvMarker,
    val refreshTop: (items: List<TreeItem<AvValueId>>) -> Unit
) {

    private var topIds = emptyList<AvValueId>()

    private val nodeMap = mutableMapOf<AvValueId, Node>()

    private class Node(
        var aioItem: AvItem<*>? = null,
        var childIds: List<AvValueId>? = null,
        var treeItem: TreeItem<AvValueId>? = null
    )

    val localWorker = adapter.firstImpl<AvValueWorker>()
    val remoteService = getService<AvValueApi>(transport)

    val conditions = listOf(
        // When loading items we don't use the value of the item selection marker,
        // only the fact that there is a marker on the item.
        AvSubscribeCondition(marker = itemMarker, itemOnly = true),
        AvSubscribeCondition(marker = childListMarker),
        AvSubscribeCondition(marker = topListMarker)
    )

    var remoteSubscriptionId: AvValueSubscriptionId? = null
    val localSubscriptionId: AvValueSubscriptionId = UUID.Companion.uuid4()

    // Tree update operations received from the server
    val updates = Channel<AvValueOperation>(Channel.Factory.UNLIMITED)

    private var topRefresh = false
    private val childRefresh = mutableSetOf<Node>()

    val size
        get() = nodeMap.size

    val topItemsSize
        get() = topIds.size

    operator fun get(valueId: AvValueId) = nodeMap[valueId]?.aioItem

    /**
     * The sub items list that contains [childId]. This is the value of the
     * sub items marker from the parent node or the top items list if
     * [childId] is a top node.
     */
    fun getParentSubItems(childId: AvValueId): List<AvValueId> {
        val aioItem = nodeMap[childId]?.aioItem ?: return emptyList()
        if (aioItem.parentId == null) return topIds
        val parent = nodeMap[aioItem.parentId] ?: return emptyList()
        return parent.childIds ?: emptyList()
    }

    /**
     * The sub items list of [childId].
     */
    fun getSubItems(childId: AvValueId): List<AvValueId> {
        return nodeMap[childId]?.childIds ?: return emptyList()
    }

    fun start() {
        localWorker.subscribe(
            AvChannelSubscription(localSubscriptionId, conditions, updates)
        )
        scope.launch {
            supervisorScope {
                remoteSubscriptionId = remoteService.subscribe(conditions)
                run()
            }
        }
    }

    fun stop() {
        localWorker.unsubscribe(localSubscriptionId)
        scope.launch {
            supervisorScope {
                updates.close()
                remoteService.unsubscribe(remoteSubscriptionId !!)
            }
        }
    }

    /**
     * Each incoming update is processed these steps:
     *
     * - First, it is applied to [topIds] and [nodeMap].
     * - Second, all changed children lists are refreshed.
     * - Third, top nodes list is refreshed if necessary.
     */
    suspend fun run() {
        for (transaction in updates) {

            transaction.forEach { operation ->
                when (operation) {
                    is AvoAddOrUpdate -> process(operation.value)
                    is AvoAdd -> process(operation.value)
                    is AvoUpdate -> process(operation.value)
                    is AvoMarkerRemove -> TODO()
                    else -> Unit // forEach flattens the transactions, compute is not handled here
                }
            }

            refresh()

            childRefresh.clear()
            topRefresh = false
        }
    }

    fun process(value: AvValue) {
        when (value) {
            is AvItem<*> -> process(value)
            is AvItemIdList -> process(value)
        }
    }

    private fun process(item: AvItem<*>) {
        val node = nodeMap.getOrPut(item.uuid) { Node() }

        node.aioItem = item

        val parentId = item.parentId
        val treeItem = node.treeItem

        if (treeItem == null) {
            val parentNode = parentId?.let { nodeMap[it] }

            node.treeItem = TreeItem(
                iconFor(item),
                title = item.name,
                data = item.uuid,
                parent = parentNode?.treeItem,
            )

            if (parentNode != null) {
                childRefresh += parentNode
            } else {
                topRefresh = true
            }
        } else {
            treeItem.title = item.name // treeItem is observable
        }
    }

    private fun process(list: AvItemIdList) {
        when (list.markerName) {
            childListMarker -> {
                val node = nodeMap.getOrPut(list.parentId) { Node() }
                node.childIds = list.itemIds
                childRefresh += node
            }

            topListMarker -> {
                topIds = list.itemIds
                topRefresh = true
            }
        }
    }

    /**
     * When refresh is called, all nodes are updated from the
     * incoming transaction. This means that we have the most
     * complete data available at this point:
     *
     * - all [Node] instances are in the [nodeMap]
     * - all possible [TreeItem] instances are created
     */
    fun refresh() {
        childRefresh.forEach { node ->
            node.treeItem?.let { ti ->
                ti.children = node.childIds?.mapNotNull { nodeMap[it]?.treeItem } ?: emptyList()
            }
        }

        if (topRefresh) {
            refreshTop(topIds.mapNotNull { nodeMap[it]?.treeItem })
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
}