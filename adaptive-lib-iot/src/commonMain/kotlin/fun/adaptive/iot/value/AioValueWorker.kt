package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AioMarkerValue
import `fun`.adaptive.iot.value.operation.*
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

    private val markerSubscriptions = mutableMapOf<AioMarker, MutableList<MarkerSubscriptionEntry>>()

    private val markerIndices = mutableMapOf<AioMarker, MutableSet<AioValueId>>()

    private val trace = true

    @OptIn(ExperimentalCoroutinesApi::class)
    val isIdle: Boolean
        get() = operations.isEmpty

    class MarkerSubscriptionEntry(
        val subscription: AioValueSubscription,
        val itemOnly: Boolean
    )

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
                    is AvoMarkerRemove -> Unit // used by subscribers, for worker it is no-op
                }

                commit(commitSet)
            }
        }
    }

    private fun add(operation: AvoAdd, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value

        val original = values.put(value.uuid, value)

        if (original != null) {
            original.let { values[it.uuid] = it } // rollback the replacement
            operation.fail()
            return
        }

        indexAndNotify(original, value, operation, commitSet)
    }

    private fun update(operation: AvoUpdate, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value
        val original = values.put(value.uuid, value)

        if (original == null || original.timestamp > value.timestamp) {
            original?.let { values[it.uuid] = it } // rollback the replacement
            operation.fail()
            return
        }

        indexAndNotify(original, value, operation, commitSet)
    }

    private fun addOrUpdate(operation: AvoAddOrUpdate, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value
        val original = values.put(value.uuid, value)

        indexAndNotify(original, value, operation, commitSet)
    }

    private fun transaction(transaction: AvoTransaction, commitSet: MutableSet<AioValueSubscription>) {
        for (operation in transaction.operations) {
            when (operation) {
                is AvoAddOrUpdate -> addOrUpdate(operation, commitSet)
                is AvoUpdate -> update(operation, commitSet)
                is AvoAdd -> add(operation, commitSet)
                is AvoTransaction -> transaction(operation, commitSet)
                is AvoMarkerRemove -> Unit // used by subscribers, for worker it is no-op
            }
        }

        transaction.success()
    }

    private fun indexAndNotify(original: AioValue?, value: AioValue, operation: AioValueOperation, commitSet: MutableSet<AioValueSubscription>) {
        index(original, value, commitSet)
        notify(value, commitSet)
        operation.success()
    }

    // --------------------------------------------------------------------------------
    // Notification (for remove marker check indexing also)
    // --------------------------------------------------------------------------------

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
            it.ifApplicable(value) { add(it.subscription, value, commitSet) }
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
        commitSet.clear()
    }

    // --------------------------------------------------------------------------------
    // Indexing and remove marker notification
    // --------------------------------------------------------------------------------

    private fun index(original: AioValue?, value: AioValue, commitSet: MutableSet<AioValueSubscription>) {
        when (value) {

            is AioItem -> {
                index(
                    value,
                    (original as? AioItem)?.markers?.keys ?: emptySet(),
                    value.markers.keys,
                    commitSet
                )
            }

            is AioMarkerValue -> {
                markerIndices[value.markerName]?.add(value.uuid)
            }
        }
    }

    private fun index(
        value: AioValue,
        originalMarkers: Set<String>,
        markers: Set<String>,
        commitSet: MutableSet<AioValueSubscription>
    ) {
        val valueId = value.uuid

        for (marker in markers) {
            if (marker !in originalMarkers) {
                markerIndices.getOrPut(marker) { mutableSetOf() }.add(valueId)
            }
        }
        for (marker in originalMarkers) {
            if (marker !in markers) {
                markerIndices[marker]?.remove(valueId)
                markerSubscriptions[marker]?.forEach {
                    it.ifApplicable(value) { markerRemove(it.subscription, valueId, marker, commitSet) }
                }
            }
        }
    }

    private fun markerRemove(
        subscription: AioValueSubscription,
        valueId: AioValueId,
        marker: AioMarker,
        commitSet: MutableSet<AioValueSubscription>
    ) {
        try {
            subscription.markerRemove(valueId, marker)
            commitSet.add(subscription)
        } catch (e: Exception) {
            logger.warning(e)
            commitSet.remove(subscription)
            unsubscribe(subscription.uuid)
        }
    }

    private inline fun MarkerSubscriptionEntry.ifApplicable(value : AioValue, block: () -> Unit) {
        if (itemOnly && value !is AioItem) return
        block()
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

        for (condition in subscription.conditions) {
            if (condition.valueId != null) {
                addValueIdSubscription(subscription, condition.valueId)
            }
            if (condition.marker != null) {
                addMarkerSubscription(subscription, condition.marker, condition.itemOnly)
            }
        }

        subscription.commit()
    }

    private fun addValueIdSubscription(subscription: AioValueSubscription, valueId: AioValueId) {
        this.valueIdSubscriptions
            .getOrPut(valueId) { mutableListOf() }
            .add(subscription)

        values[valueId]?.let {
            subscription.add(it)
        }
    }

    private fun addMarkerSubscription(subscription: AioValueSubscription, marker: AioMarker, itemOnly: Boolean) {
        this.markerSubscriptions
            .getOrPut(marker) { mutableListOf() }
            .add(MarkerSubscriptionEntry(subscription, itemOnly))

        markerIndices[marker]?.let { index ->
            for (valueId in index) {
                values[valueId]?.let { value ->
                    subscription.add(value)
                }
            }
        }
    }

    fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        lock.use {
            unsafeUnsubscribe(subscriptionId)
        }
    }

    private fun unsafeUnsubscribe(subscriptionId: AuiValueSubscriptionId) {
        subscriptions.remove(subscriptionId)?.let { subscription ->

            for (condition in subscription.conditions) {
                condition.valueId?.let { valueId ->
                    valueIdSubscriptions[valueId]?.remove(subscription)
                }
                condition.marker?.let { marker ->
                    markerSubscriptions[marker]?.removeAll {
                        entry -> entry.subscription.uuid == subscriptionId
                    }
                }
            }

            subscription.worker = null
        }
    }

    fun subscriptionCount(valueId: AioValueId): Int =
        lock.use {
            valueIdSubscriptions[valueId]?.size ?: 0
        }

    // --------------------------------------------------------------------------------
    // Query
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

    fun queryByMarker(marker: AioMarker): List<AioValue> =
        lock.use {
            markerIndices[marker]?.mapNotNull { values[it] } ?: emptyList()
        }

    // --------------------------------------------------------------------------------
    // Update
    // --------------------------------------------------------------------------------

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