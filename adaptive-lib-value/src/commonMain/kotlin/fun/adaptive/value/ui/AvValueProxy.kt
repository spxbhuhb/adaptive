package `fun`.adaptive.value.ui

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.*
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AvValueProxy(
    adapter: BackendAdapter,
    transport: ServiceCallTransport,
    val scope: CoroutineScope,
    val valueId: AvValueId
) {

    private val valueMap = mutableMapOf<AvValueId, AvValue>()

    val localWorker = adapter.firstImpl<AvValueWorker>()
    val remoteService = getService<AvValueApi>(transport)

    val conditions = listOf(
        // When loading items we don't use the value of the item selection marker,
        // only the fact that there is a marker on the item.
        AvSubscribeCondition(valueId = valueId)
    )

    var remoteSubscriptionId: AvValueSubscriptionId? = null
    val localSubscriptionId: AvValueSubscriptionId = UUID.Companion.uuid4()

    val updates = Channel<AvValueOperation>(Channel.Factory.UNLIMITED)

    operator fun get(valueId: AvValueId) = valueMap[valueId]

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
        }
    }

    fun process(value: AvValue) {
        valueMap[value.uuid] = value
    }
}