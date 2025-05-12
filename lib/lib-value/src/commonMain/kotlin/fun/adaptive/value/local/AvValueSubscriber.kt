package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.general.AbstractObservable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.value.*
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.coroutineContext

/**
 * Base class for client-side classes that subscribe for values and
 * notify the attached observers when a value or an update arrives.
 *
 * - Calls [subscribeFun] to subscribe to the data on the remote provider.
 * - Sends the subscription to the local value worker to receive updates.
 * - Calls [process] when a value or an update arrives.
 *
 * Override [process] to process the incoming data into [V].
 *
 * Call [notifyListeners] when [AbstractObservable.value] changes.
 *
 * **Lifecycle**
 *
 * - [addListener] subscribes when there are no other listeners
 * - [removeListener] unsubscribes when there are no listeners remaining
 */
abstract class AvValueSubscriber<V>(
    val subscribeFun: AvSubscribeFun,
    backend: BackendAdapter
) : AbstractObservable<V>() {

    val remoteService = getService<AvValueApi>(backend.transport)
    val localWorker = backend.firstImpl<AvValueWorker>()
    val scope = backend.scope

    var job: Job? = null

    override fun addListener(listener: ObservableListener<V>) {
        // TODO refactor the subscriber pattern so that add/remove/add listeners don't need to be synchronized'
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
        val updates = Channel<AvValueOperation>(Channel.Factory.UNLIMITED)

        val subscriptionId = AvSubscriptionId()

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
        } catch (ex: Exception) {
            coroutineContext.ensureActive()
            getLogger("AvValueSubscriber").error(ex)
        } finally {
            localWorker.unsubscribe(subscriptionId)
            scope.launch { supervisorScope { unsubscribe(subscriptionId); job = null } }
        }
    }

    suspend fun subscribe(id: AvSubscriptionId): List<AvSubscribeCondition> =
        subscribeFun(remoteService, id)

    suspend fun unsubscribe(id: AvSubscriptionId) =
        remoteService.unsubscribe(id)

    /**
     * Called before subscription when the first listener is attached. (May be called
     * more than once if you add and remove listeners).
     */
    open fun onStart() = Unit

    /**
     * Called after an update is processed.
     */
    open fun onCommit() = Unit

    /**
     * Process an incoming value.
     */
    abstract fun process(value: AvValue<*>)

    fun stop() {
        listeners.clear()
        job?.cancel()
    }
}