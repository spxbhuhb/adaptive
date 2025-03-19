package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AioMarkerValue
import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.iot.value.operation.AvoAdd
import `fun`.adaptive.iot.value.operation.AvoAddOrUpdate
import `fun`.adaptive.iot.value.operation.AvoTransaction
import `fun`.adaptive.iot.value.operation.AvoUpdate
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel

class AioValueWorker internal constructor() : WorkerImpl<AioValueWorker> {

    private val lock = getLock()

    private val operations = Channel<AioValueOperation>(Channel.UNLIMITED)

    private val values = mutableMapOf<AioValueId, AioValue>()

    private val subscriptions = mutableMapOf<AuiValueSubscriptionId, AioValueSubscription>()

    private val valueIdSubscriptions = mutableMapOf<AioValueId, MutableList<AioValueSubscription>>()

    private val markerSubscriptions = mutableMapOf<AioMarker, MutableList<AioValueSubscription>>()

    private val trace = true

    @OptIn(ExperimentalCoroutinesApi::class)
    val isIdle: Boolean
        get() = operations.isEmpty

    // --------------------------------------------------------------------------------
    // Operations
    // --------------------------------------------------------------------------------

    override suspend fun run() {
        if (trace) logger.enableFine()

        for (operation in operations) {
            lock.use {
                if (trace) logger.fine("Operation: $operation")

                val commitSet = mutableSetOf<AioValueSubscription>()

                when (operation) {
                    is AvoAddOrUpdate -> addOrUpdate(operation, commitSet)
                    is AvoTransaction -> transaction(operation, commitSet)
                    is AvoUpdate -> update(operation, commitSet)
                    is AvoAdd -> add(operation, commitSet)
                }

                commit(commitSet)
            }
        }
    }

    private fun add(operation: AvoAdd, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value
        val current = values[value.uuid]

        if (current != null) {
            operation.fail()
            return
        }

        values[value.uuid] = value

        notify(value, commitSet)

        operation.success()
    }

    private fun update(operation: AvoUpdate, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value
        val current = values[value.uuid]

        if (current == null || current.timestamp > value.timestamp) {
            operation.fail()
            return
        }

        values[value.uuid] = value

        notify(value, commitSet)

        operation.success()
    }

    private fun addOrUpdate(operation: AvoAddOrUpdate, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value
        val current = values[value.uuid]

        if (current == null || current.timestamp <= value.timestamp) {
            values[value.uuid] = value
            notify(value, commitSet)
        }

        operation.success()
    }

    private fun transaction(transaction: AvoTransaction, commitSet: MutableSet<AioValueSubscription>) {
        for (operation in transaction.operations) {
            when (operation) {
                is AvoAddOrUpdate -> addOrUpdate(operation, commitSet)
                is AvoUpdate -> update(operation, commitSet)
                is AvoAdd -> add(operation, commitSet)
                is AvoTransaction -> transaction(operation, commitSet)
            }
        }

        transaction.success()
    }

    fun notify(value: AioValue, commitSet: MutableSet<AioValueSubscription>) {
        valueIdSubscriptions[value.uuid]?.forEach {
            add(it, value, commitSet)
        }

        notifyByMarker(value, commitSet)
    }

    fun notifyByMarker(value: AioValue, commitSet: MutableSet<AioValueSubscription>) {
        when (value) {
            is AioItem -> value.markersOrNull?.forEach { marker -> notifyByMarker(marker.key, value, commitSet) }
            is AioMarkerValue -> notifyByMarker(value.markerName, value, commitSet)
        }
    }

    fun notifyByMarker(markerName: String, value: AioValue, commitSet: MutableSet<AioValueSubscription>) {
        markerSubscriptions[markerName]?.forEach {
            add(it, value, commitSet)
        }
    }

    private fun add(subscription: AioValueSubscription, value: AioValue, commitSet: MutableSet<AioValueSubscription>) {
        try {
            subscription.add(value)
            commitSet.add(subscription)
        } catch (e: Exception) {
            logger.warning(e)
            commitSet.remove(subscription)
            unsubscribe(subscription.uuid)
        }
    }

    fun commit(commitSet: MutableSet<AioValueSubscription>) {
        commitSet.forEach { subscription ->
            try {
                subscription.commit()
            } catch (e: Exception) {
                logger.warning(e)
                unsubscribe(subscription.uuid)
            }
        }
    }

    // --------------------------------------------------------------------------------
    // Subscription management
    // --------------------------------------------------------------------------------



    fun subscribe(subscription: AioValueSubscription) {
        lock.use {
            unsafeSubscribe(subscription)
        }
    }

    fun subscribe(subscriptions: List<AioValueSubscription>) {
        lock.use {
            for (subscription in subscriptions) {
                unsafeSubscribe(subscription)
            }
        }
    }

    private fun unsafeSubscribe(subscription: AioValueSubscription) {
        subscription.worker = this

        this.subscriptions[subscription.uuid] = subscription

        for (valueId in subscription.valueIds) {
            this.valueIdSubscriptions
                .getOrPut(valueId) { mutableListOf() }
                .add(subscription)

            values[valueId]?.let {
                subscription.add(it)
            }
        }

        for (marker in subscription.markers) {
            this.markerSubscriptions
                .getOrPut(marker) { mutableListOf() }
                .add(subscription)
        }

        subscription.commit()
    }

    fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        lock.use {
            unsafeUnsubscribe(subscriptionId)
        }
    }

    private fun unsafeUnsubscribe(subscriptionId: AuiValueSubscriptionId) {
        subscriptions.remove(subscriptionId)?.let {

            for (valueId in it.valueIds) {
                valueIdSubscriptions[valueId]?.remove(it)
            }

            for (marker in it.markers) {
                markerSubscriptions[marker]?.remove(it)
            }

            it.worker = null
        }
    }

    fun subscriptionCount(valueId: AioValueId): Int =
        lock.use {
            valueIdSubscriptions[valueId]?.size ?: 0
        }

    // --------------------------------------------------------------------------------
    // Access
    // --------------------------------------------------------------------------------

    operator fun get(valueId: AioValueId): AioValue? =
        lock.use {
            values[valueId]
        }

    fun item(valueId: AioValueId): AioItem =
        get(valueId) as AioItem

    fun itemOrNull(valueId: AioValueId): AioItem? =
        get(valueId)?.let { it as AioItem }

    fun query(filterFun: (AioValue) -> Boolean): List<AioValue> =
        lock.use {
            values.values.filter(filterFun)
        }

    fun add(value: AioValue) {
        check(operations.trySend(AvoAdd(value)).isSuccess)
    }

    fun addAll(vararg values: AioValue) =
        transaction(values.map { AvoAdd(it) })

    fun update(value: AioValue) {
        check(operations.trySend(AvoUpdate(value)).isSuccess)
    }

    fun addOrUpdate(value: AioValue) {
        check(operations.trySend(AvoAddOrUpdate(value)).isSuccess)
    }

    fun transaction(operations: List<AioValueOperation>) {
        check(this.operations.trySend(AvoTransaction(operations)).isSuccess)
    }

    fun process(operation: AioValueOperation) {
        check(this.operations.trySend(operation).isSuccess)
    }

    // --------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------

    fun close() {
        operations.close()
    }

}