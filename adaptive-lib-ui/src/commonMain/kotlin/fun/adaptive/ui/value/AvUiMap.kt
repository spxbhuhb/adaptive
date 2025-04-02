package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.general.Observable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.*
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

abstract class AvUiMap<K,V>(
    val backend: BackendAdapter,
) : Observable<Map<K,V>>() {

    override var value: Map<K,V>
        get() = valueMap
        set(_) = unsupported()

    private val valueMap = mutableMapOf<K, V>()

    val transport: ServiceCallTransport = backend.transport
    val scope: CoroutineScope = backend.scope

    val localWorker = backend.firstImpl<AvValueWorker>()
    val remoteService = getService<AvValueApi>(transport)

    var remoteSubscriptionId: AvValueSubscriptionId? = null
    var localSubscriptionId: AvValueSubscriptionId? = null

    val updates = Channel<AvValueOperation>(Channel.UNLIMITED)

    operator fun get(valueId: K) = valueMap[valueId]

    var isRunning = false

    override fun addListener(listener: ObservableListener<Map<K,V>>) {
        if (listeners.isEmpty()) {
            start()
            renew()
        }
        super.addListener(listener)
    }

    override fun removeListener(listener: ObservableListener<Map<K,V>>) {
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

    abstract fun process(value: AvValue)

    abstract fun conditions(): MutableList<AvSubscribeCondition>

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