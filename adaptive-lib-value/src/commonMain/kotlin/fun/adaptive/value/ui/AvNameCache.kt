package `fun`.adaptive.value.ui

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.general.Observable
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AvNameCache(
    adapter: BackendAdapter,
    transport: ServiceCallTransport,
    val scope: CoroutineScope,
    itemMarker: AvMarker
) : Observable<List<AvNameCacheEntry>>() {


    override var value: List<AvNameCacheEntry> = emptyList()
        set(v) {
            field = v
            notifyListeners()
        }

    private val itemMap = mutableMapOf<AvValueId, AvItem<*>>()

    val localWorker = adapter.firstImpl<AvValueWorker>()
    val remoteService = getService<AvValueApi>(transport)

    val conditions = listOf(
        // When loading items we don't use the value of the item selection marker,
        // only the fact that there is a marker on the item.
        AvSubscribeCondition(marker = itemMarker, itemOnly = true)
    )

    var remoteSubscriptionId: AvValueSubscriptionId? = null
    val localSubscriptionId: AvValueSubscriptionId = UUID.Companion.uuid4()

    val updates = Channel<AvValueOperation>(Channel.UNLIMITED)

    val size
        get() = itemMap.size

    operator fun get(valueId: AvValueId) = itemMap[valueId]

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
        }
    }

    fun process(value: AvValue) {
        check(value is AvItem<*>)
        itemMap[value.uuid] = value
    }

    fun pathNames(item : AvItem<*>): List<String> {
        var parentId = item.parentId
        val names = mutableListOf<String>(item.name)

        while (parentId != null) {
            val parent = itemMap[parentId] ?: return names.reversed()
            names += parent.name
            parentId = parent.parentId
        }

        return names.reversed()
    }

    fun refresh() {
        val new = mutableListOf<AvNameCacheEntry>()

        for (item in itemMap.values) {
            new += AvNameCacheEntry(item.uuid, pathNames(item))
        }

        value = new.sortedBy { it.names.joinToString(".") }
    }
}