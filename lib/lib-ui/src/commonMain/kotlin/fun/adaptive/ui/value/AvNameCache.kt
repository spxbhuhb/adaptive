package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.general.AbstractObservable
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.channels.Channel
import kotlin.collections.plusAssign

class AvNameCache(
    adapter : BackendAdapter,
    transport : ServiceCallTransport,
    val workspace : FrontendWorkspace,
    itemMarker : AvMarker,
    val parentRefLabel : AvRefLabel
) : AbstractObservable<List<AvNameCacheEntry>>() {


    override var value : List<AvNameCacheEntry> = emptyList()
        set(v) {
            field = v
            notifyListeners()
        }

    private val itemMap = mutableMapOf<AvValueId, AvValue<*>>()

    val localWorker = adapter.firstImpl<AvValueWorker>()
    val remoteService = getService<AvValueApi>(transport)

    val conditions = listOf(
        // When loading items we don't use the value of the item selection marker,
        // only the fact that there is a marker on the item.
        AvSubscribeCondition(marker = itemMarker, itemOnly = true)
    )

    var remoteSubscriptionId : AvSubscriptionId? = null
    val localSubscriptionId : AvSubscriptionId = UUID.Companion.uuid4()

    val updates = Channel<AvValueOperation>(Channel.UNLIMITED)

    val size
        get() = itemMap.size

    operator fun get(valueId : AvValueId) = itemMap[valueId]

    fun start() {
        localWorker.subscribe(
            AvChannelSubscription(localSubscriptionId, conditions, updates)
        )
        workspace.io {
            remoteSubscriptionId = remoteService.subscribe(conditions)
            run()
        }
    }

    fun stop() {
        localWorker.unsubscribe(localSubscriptionId)
        workspace.io {
            updates.close()
            remoteService.unsubscribe(remoteSubscriptionId !!)
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

    fun process(value : AvValue<*>) {
        itemMap[value.uuid] = value
    }

    fun pathNames(value : AvValue<*>) : List<String> {
        var parentId = value.refIdOrNull(parentRefLabel)
        val names = mutableListOf(value.name ?: "?")

        while (parentId != null) {
            val parent = itemMap[parentId] ?: return names.reversed()
            names += parent.name ?: "?"
            parentId = parent.refIdOrNull(parentRefLabel)
        }

        return names.reversed()
    }

    fun refresh() {
        val new = mutableListOf<AvNameCacheEntry>()

        for (item in itemMap.values) {
            new += AvNameCacheEntry(item, pathNames(item))
        }

        value = new.sortedBy { it.names.joinToString(".") }
    }
}