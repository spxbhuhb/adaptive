package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.general.AbstractObservable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.log.getLogger
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
import kotlin.reflect.KClass

class AvUiValue<T :Any>(
    adapter: AdaptiveAdapter,
    val valueId: AvValueId?,
    val kClass : KClass<T>
) : AbstractObservable<AvValue<T>?>() {

    companion object {
        inline operator fun <reified T : Any> invoke(
            adapter: AdaptiveAdapter,
            valueId: AvValueId?
        ) : AvUiValue<T> = AvUiValue(adapter, valueId, T::class)
    }

    override var value: AvValue<T>? = null
        set(v) {
            field = v
            notifyListeners()
        }

    val backend = adapter.backend
    val transport: ServiceCallTransport = adapter.transport
    val scope: CoroutineScope = adapter.scope

    val localWorker = backend.firstImpl<AvValueWorker>()
    val remoteService = getService<AvValueApi>(transport)

    var remoteSubscriptionId: AvSubscriptionId? = null
    var localSubscriptionId: AvSubscriptionId? = null

    val updates = Channel<AvValueOperation>(Channel.UNLIMITED)

    var isRunning = false

    override fun addListener(listener: ObservableListener<AvValue<T>?>) {
        if (valueId != null && listeners.isEmpty()) {
            start()
            renew()
        }
        super.addListener(listener)
    }

    override fun removeListener(listener: ObservableListener<AvValue<T>?>) {
        super.removeListener(listener)
        if (valueId != null && listeners.isEmpty()) {
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
        }
    }

    fun process(value: AvValue<*>) {
        if (kClass.isInstance(value.spec)) {
            @Suppress("UNCHECKED_CAST")
            this.value = value.checkSpec(kClass)
        } else {
            getLogger("AvUiValue").warning("dropping value as it is not of type ${kClass.simpleName}: $value")
        }
    }

    fun renew() {
        val conditions = listOf(AvSubscribeCondition(valueId))
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