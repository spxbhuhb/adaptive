package `fun`.adaptive.value.remote

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.general.AbstractObservable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.value.*
import `fun`.adaptive.value.operation.*
import kotlinx.coroutines.CoroutineScope
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
abstract class AvAbstractRemoteSubscriber<V>(
    val subscribeFun: AvSubscribeFun,
    backend: BackendAdapter,
    override val binding: AdaptiveStateVariableBinding<V>? = null
) : AbstractObservable<V>(), AdaptiveProducer<V>  {

    val remoteService = getService<AvValueApi>(backend.transport)
    val localWorker = backend.firstImpl<AvValueWorker>()

    val frontendScope = binding?.let { CoroutineScope(it.targetFragment.adapter.dispatcher) }
    val backendScope = backend.scope

    var job: Job? = null

    override var latestValue: V?
        get() = value
        set(value) { unsupported() }

    override fun start() {
        onStart()
        job = backendScope.launch { supervisorScope { run() } }
    }

    override fun stop() {
        listeners.clear()
        job?.cancel()
    }

    override fun addListener(listener: ObservableListener<V>) {
        // TODO refactor the subscriber pattern so that add/remove/add listeners don't need to be synchronized'
        if (job == null) start()
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
            getLogger("AvAbstractRemoteSubscriber").error(ex)
        } finally {
            localWorker.unsubscribe(subscriptionId)
            backendScope.launch { supervisorScope { unsubscribe(subscriptionId); job = null } }
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
    open fun onCommit() {
        notifyListeners()
        frontendScope?.launch {
            setDirtyBatch()
        }
    }

    /**
     * Process an incoming value.
     */
    abstract fun process(value: AvValue<*>)

}