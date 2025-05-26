package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.general.AbstractObservable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AvUiList(
    adapter: AdaptiveAdapter,
    val valueId: AvValueId,
    val listMarker: AvMarker
) : AbstractObservable<List<AvValue<*>>>() {

    override var value: List<AvValue<*>>
        get() = list.mapNotNull { valueMap[it] }.sortedBy { it.friendlyId }
        set(_) = unsupported()

    val backend = adapter.backend
    val transport: ServiceCallTransport = adapter.transport
    val scope: CoroutineScope = adapter.scope

    private var listId: AvValueId? = null
    private var list = emptyList<AvValueId>()

    private val valueMap = mutableMapOf<AvValueId, AvValue<*>>()

    val localWorker = backend.firstImpl<AvValueWorker>()
    val remoteService = getService<AvValueApi>(transport)

    var remoteSubscriptionId: AvSubscriptionId? = null
    var localSubscriptionId: AvSubscriptionId? = null

    val updates = Channel<AvValueOperation>(Channel.UNLIMITED)

    operator fun get(valueId: AvValueId) = valueMap[valueId]

    var isRunning = false

    override fun addListener(listener: ObservableListener<List<AvValue<*>>>) {
        if (listeners.isEmpty()) {
            start()
            renew()
        }
        super.addListener(listener)
    }

    override fun removeListener(listener: ObservableListener<List<AvValue<*>>>) {
        super.removeListener(listener)
        if (listeners.isEmpty()) {
            renewLocal(null)
            renewRemote(null)
            updates.close()
        }
    }

    fun start() {
        if (! isRunning) {
            isRunning = true
            scope.launch {
                supervisorScope {
                    run()
                }
            }
        }
    }

    suspend fun run() {
        for (update in updates) {
            update.forEach { operation ->
                when (operation) {
                    is AvoAddOrUpdate -> process(operation.value)
                    is AvoAdd -> process(operation.value)
                    is AvoUpdate -> process(operation.value)
                    is AvoMarkerRemove -> TODO()
                    else -> Unit // forEach flattens the transactions, compute is not handled here
                }
            }

            notifyListeners()
        }
    }

    fun process(value: AvValue<*>) {
        when (value.uuid) {
            valueId -> processValue(value)
            listId -> processList(value.checkSpec())
            else -> valueMap[value.uuid] = value
        }
    }

    fun processValue(value: AvValue<*>) {
        val listId = value.refIdOrNull(listMarker)

        if (listId != this.listId) {
            this.listId = listId
            this.list = emptyList()
            renew()
        }
    }

    fun processList(value: AvValue<AvRefListSpec>) {
        if (value.uuid == listId && list != value.refs) {
            list = value.spec.refs
            renew()
        }
    }

    fun conditions(): MutableList<AvSubscribeCondition> {
        val conditions = mutableListOf<AvSubscribeCondition>()

        conditions += AvSubscribeCondition(valueId)

        listId?.let { safeListId ->
            conditions += AvSubscribeCondition(safeListId)
            conditions += list.map { AvSubscribeCondition(it) }
        }

        return conditions
    }

    fun renew() {
        val conditions = conditions()
        renewLocal(conditions)
        renewRemote(conditions)
    }

    fun renewLocal(conditions: List<AvSubscribeCondition>?) {
        localSubscriptionId?.let { safeLocalSubscriptionId ->
            localWorker.unsubscribe(safeLocalSubscriptionId)
        }

        val newLocalId = uuid4<AvSubscription>()

        conditions?.let {
            localWorker.subscribe(
                AvChannelSubscription(newLocalId, conditions, updates)
            )
        }
    }

    fun renewRemote(conditions: List<AvSubscribeCondition>?) {
        val currentRemote = remoteSubscriptionId

        scope.launch {
            supervisorScope {
                currentRemote?.let { remoteService.unsubscribe(it) }
                conditions?.let { remoteService.subscribe(it) }
            }
        }
    }

}