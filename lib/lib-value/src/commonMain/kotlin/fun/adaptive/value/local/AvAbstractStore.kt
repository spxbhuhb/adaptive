package `fun`.adaptive.value.local

import `fun`.adaptive.general.AbstractObservable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.value.*
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * A generic store that contains data from a remote data provider.
 *
 * [subscribe] and [unsubscribe] is used to initiate and close connection
 * to the data provider.
 *
 * Override [process] to process the incoming data into [V].
 *
 * Call [notifyListeners] when [AbstractObservable.value] changes.
 *
 * **Lifecycle**
 *
 * - [addListener] subscribes when there are no other listeners
 * - [removeListener]
 * - When there is at least one listener, the store is active. When the last listener is removed
 * the store stops.
 */
abstract class AvAbstractStore<V>(
    val publisher: AvPublisher,
    val scope: CoroutineScope,
    val localWorker: AvValueWorker,
) : AbstractObservable<V>() {

    var job: Job? = null

    override fun addListener(listener: ObservableListener<V>) {
        if (job == null) {
            onStart()
            job = scope.launch { supervisorScope { run() } }
        }
        super.addListener(listener)
    }

    override fun removeListener(listener: ObservableListener<V>) {
        super.removeListener(listener)
        if (listeners.isEmpty()) {
            job?.cancel()
        }
    }

    suspend fun run() {
        var updates = Channel<AvValueOperation>(Channel.Factory.UNLIMITED)

        val subscriptionId = AvValueSubscriptionId()

        val conditions = subscribe(subscriptionId)

        localWorker.subscribe(AvChannelSubscription(subscriptionId, conditions, updates))

        try {
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
                onCommit()
            }
        } finally {
            localWorker.unsubscribe(subscriptionId)
            scope.launch { supervisorScope { unsubscribe(subscriptionId) } }
        }
    }

    suspend fun subscribe(id: AvValueSubscriptionId): List<AvSubscribeCondition> =
        publisher.subscribe(id)

    suspend fun unsubscribe(id: AvValueSubscriptionId) =
        publisher.unsubscribe(id)

    open fun onStart() = Unit

    open fun onStop() = Unit

    open fun onCommit() = Unit

    abstract fun process(value: AvValue)

}