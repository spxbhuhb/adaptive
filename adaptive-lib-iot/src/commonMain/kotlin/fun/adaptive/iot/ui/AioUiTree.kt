package `fun`.adaptive.iot.ui

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.value.*
import `fun`.adaptive.iot.value.operation.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.builtin.empty
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.utility.UUID.Companion.uuid4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AioUiTree(
    adapter: BackendAdapter,
    transport: ServiceCallTransport,
    val scope: CoroutineScope,
    val itemMarker: AioMarker,
    val childListMarker: AioMarker,
    val topListMarker: AioMarker,
    val refreshTop: (items: List<TreeItem<AioValueId>>) -> Unit
) {

    private var topSpaces = emptyList<AioValueId>()

    private val nodeMap = mutableMapOf<AioValueId, Node>()

    private class Node(
        var aioItem: AioItem? = null,
        var subSpaces: List<AioValueId>? = null,
        var treeItem: TreeItem<AioValueId>? = null
    )

    val localWorker = adapter.firstImpl<AioValueWorker>()
    val remoteService = getService<AioValueApi>(transport)

    val markers = listOf(itemMarker, childListMarker, topListMarker)

    var remoteSubscriptionId: AuiValueSubscriptionId? = null
    val localSubscriptionId: AuiValueSubscriptionId = uuid4()

    // Tree update operations received from the server
    val updates = Channel<AioValueOperation>(Channel.UNLIMITED)

    private var topRefresh = false
    private val childRefresh = mutableSetOf<Node>()

    val size
        get() = nodeMap.size

    fun start() {
        localWorker.subscribe(
            AioValueChannelSubscription(
                localSubscriptionId,
                emptyList(),
                markers,
                updates
            )
        )
        scope.launch {
            supervisorScope {
                remoteSubscriptionId = remoteService.subscribe(emptyList(), markers)
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
     * - First, it is applied to [topSpaces] and [nodeMap].
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
                    is AvoTransaction -> Unit // forEach flattens the transactions
                }
            }

            refresh()

            childRefresh.clear()
            topRefresh = false
        }
    }

    fun process(value: AioValue) {
        when (value) {
            is AioItem -> process(value)
            is AmvItemIdList -> process(value)
        }
    }

    private fun process(item: AioItem) {
        val node = nodeMap.getOrPut(item.uuid) { Node() }

        node.aioItem = item

        val parentId = item.parentId
        val treeItem = node.treeItem

        if (treeItem == null) {
            val parentNode = parentId?.let { nodeMap[it] }

            node.treeItem = TreeItem(
                Graphics.empty,
                title = item.name,
                data = item.uuid,
                parent = parentNode?.treeItem,
            )

            if (parentNode != null) {
                childRefresh += parentNode
            }
        } else {
            treeItem.title = item.name // treeItem is observable
        }
    }

    private fun process(list: AmvItemIdList) {
        when (list.markerName) {
            childListMarker -> {
                val node = nodeMap.getOrPut(list.owner) { Node() }
                node.subSpaces = list.itemIds
                childRefresh += node
            }

            topListMarker -> {
                topSpaces = list.itemIds
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
                ti.children = node.subSpaces?.mapNotNull { nodeMap[it]?.treeItem } ?: emptyList()
            }
        }

        if (topRefresh) {
            refreshTop(topSpaces.mapNotNull { nodeMap[it]?.treeItem })
        }
    }

}