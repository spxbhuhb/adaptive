package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AioMarkerValue
import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.value.operation.*
import `fun`.adaptive.iot.value.persistence.AbstractValuePersistence
import `fun`.adaptive.iot.value.persistence.NoPersistence
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.builtin.PolymorphicWireFormat
import `fun`.adaptive.wireformat.json.JsonWireFormatEncoder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AioValueWorker internal constructor(
    val persistence: AbstractValuePersistence = NoPersistence()
) : WorkerImpl<AioValueWorker> {

    private val lock = getLock()

    private val operations = Channel<AioValueOperation>(Channel.UNLIMITED)

    private val values = mutableMapOf<AioValueId, AioValue>()

    private val subscriptions = mutableMapOf<AioValueSubscriptionId, AioValueSubscription>()

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

    @ExperimentalCoroutinesApi
    override suspend fun run() {
        if (trace) logger.enableFine()

        lock.use {
            check(values.isEmpty())
            check(subscriptions.isEmpty())
            check(markerIndices.isEmpty())
            check(operations.isEmpty)

            persistence.loadValues(values)

            // this will be empty as there are no subscriptions
            val commitSet = mutableSetOf<AioValueSubscription>()

            values.forEach { (_, value) ->
                index(null, value, commitSet)
            }
        }

        for (operation in operations) {
            lock.use {
                if (trace) logger.fine("Operation: $operation")

                val commitSet = mutableSetOf<AioValueSubscription>()

                when (operation) {
                    is AvoAddOrUpdate -> addOrUpdate(operation, commitSet)
                    is AvoTransaction -> transaction(operation, commitSet)
                    is AvoComputation<*> -> compute(operation, commitSet)
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
            operation.fail("value with id ${value.uuid} already exists")
            return
        }

        persistence.saveValue(value)
        indexAndNotify(original, value, operation, commitSet)
    }

    private fun update(operation: AvoUpdate, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value
        val original = values.put(value.uuid, value)

        if (original == null || original.timestamp > value.timestamp) {
            original?.let { values[it.uuid] = it } // rollback the replacement
            operation.fail("value with id ${value.uuid} does not exists")
            return
        }

        persistence.saveValue(value)
        indexAndNotify(original, value, operation, commitSet)
    }

    private fun addOrUpdate(operation: AvoAddOrUpdate, commitSet: MutableSet<AioValueSubscription>) {
        val value = operation.value
        val original = values.put(value.uuid, value)

        persistence.saveValue(value)
        indexAndNotify(original, value, operation, commitSet)
    }

    private fun transaction(transaction: AvoTransaction, commitSet: MutableSet<AioValueSubscription>) {
        for (operation in transaction.operations) {
            when (operation) {
                is AvoAddOrUpdate -> addOrUpdate(operation, commitSet)
                is AvoComputation<*> -> compute(operation, commitSet)
                is AvoUpdate -> update(operation, commitSet)
                is AvoAdd -> add(operation, commitSet)
                is AvoTransaction -> transaction(operation, commitSet)
                is AvoMarkerRemove -> Unit // used by subscribers, for worker it is no-op
            }
        }

        transaction.success()
    }

    private fun compute(operation: AvoComputation<*>, commitSet: MutableSet<AioValueSubscription>) {
        try {
            val result = operation.computation?.invoke(WorkerComputeContext(commitSet))
            operation.success(result)
        } catch (e: Exception) {
            operation.fail(e)
        }
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
                markerIndices.getOrPut(value.markerName) { mutableSetOf() }.add(value.uuid)
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

    private inline fun MarkerSubscriptionEntry.ifApplicable(value: AioValue, block: () -> Unit) {
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

    fun unsubscribe(subscriptionId: AioValueSubscriptionId) {
        lock.use {
            unsafeUnsubscribe(subscriptionId)
        }
    }

    private fun unsafeUnsubscribe(subscriptionId: AioValueSubscriptionId) {
        subscriptions.remove(subscriptionId)?.let { subscription ->

            for (condition in subscription.conditions) {
                condition.valueId?.let { valueId ->
                    valueIdSubscriptions[valueId]?.remove(subscription)
                }
                condition.marker?.let { marker ->
                    markerSubscriptions[marker]?.removeAll { entry ->
                        entry.subscription.uuid == subscriptionId
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

    fun query(filterFun: (AioValue) -> Boolean): List<AioValue> =
        lock.use {
            values.values.filter(filterFun)
        }

    fun queryByMarker(marker: AioMarker): List<AioValue> =
        lock.use {
            markerIndices[marker]?.mapNotNull { values[it] } ?: emptyList()
        }

    // --------------------------------------------------------------------------------
    // Send operations to the operation queue
    // --------------------------------------------------------------------------------

    internal fun queueAdd(value: AioValue) {
        check(operations.trySend(AvoAdd(value)).isSuccess)
    }

    internal fun queueAddAll(vararg values: AioValue) =
        queueTransaction(values.map { AvoAdd(it) })

    internal fun queueUpdate(value: AioValue) {
        check(operations.trySend(AvoUpdate(value)).isSuccess)
    }

    internal fun queueAddOrUpdate(value: AioValue) {
        check(operations.trySend(AvoAddOrUpdate(value)).isSuccess)
    }

    internal fun queueTransaction(operations: List<AioValueOperation>) {
        check(this.operations.trySend(AvoTransaction(operations)).isSuccess)
    }

    internal fun queueOperation(operation: AioValueOperation) {
        check(this.operations.trySend(operation).isSuccess)
    }

    /**
     * Execute [computeFun] in the worker operation processing loop.
     *
     * The main purpose of [execute] is to perform changes on values with the guarantee that:
     *
     * - no other changes are applied meanwhile
     * - the changes together are atomic from outside point of view
     *
     * Behaviour:
     *
     * - Waits until [computeFun] is executed or timeout has been reached (this wait does not lock the worker).
     * - Throws exception if [computeFun] throws exception.
     * - **It is possible that computeFun is executed after timed out**.
     * - [computeFun] **MUST BE FAST**, it runs under the value worker lock, stops everything else
     * - rollback **MUST BE HANDLED** by [computeFun], the worker does not guarantee that
     */
    suspend fun <T> execute(timeout: Duration = 5.seconds, computeFun: AioComputeFun<T>): T {
        val channel = Channel<Any>()

        val op = AvoComputation<T>().also {
            it.channel = channel
            it.computation = computeFun
        }

        check(this.operations.trySend(op).isSuccess)

        val result = withTimeout(timeout) {
            channel.receive()
        }

        if (result is Throwable) {
            throw result
        } else {
            @Suppress("UNCHECKED_CAST")
            return result as T
        }
    }

    // --------------------------------------------------------------------------------
    // Utility
    // --------------------------------------------------------------------------------

    fun dump(): String {
        val out = StringBuilder()

        out.append("{\n  \"values\" : [\n")
        values.forEach { (_, value) ->
            val bytes = PolymorphicWireFormat.wireFormatEncode(JsonWireFormatEncoder(), value).pack()
            out.append("    ")
            out.append(bytes.decodeToString())
            out.append(",\n")
        }

        out.append("  ],\n  \"indices\" : [\n")

        markerIndices.forEach { (marker, index) ->
            out.append("    \"$marker\" : [ ")
            out.append(index.joinToString(", ") { "\"$it\"" })
        }

        out.append(" ],\n")

        out.append("}")

        return out.toString()
    }

    // --------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------

    fun close() {
        operations.close()
    }

    // --------------------------------------------------------------------------------
    // Computation support
    // --------------------------------------------------------------------------------

    inner class WorkerComputeContext(
        val commitSet: MutableSet<AioValueSubscription>
    ) {

        operator fun plusAssign(value: AioValue) {
            addOrUpdate(AvoAddOrUpdate(value), commitSet)
        }

        operator fun get(valueId: AioValueId): AioValue? =
            this@AioValueWorker[valueId]

        fun nextFriendlyId(marker: AioMarker): Int =
            queryByMarker(marker).maxOfOrNull { if (it is AioItem) it.friendlyId else 0 } ?: 0

        fun queryByMarker(marker: AioMarker): List<AioValue> =
            this@AioValueWorker.queryByMarker(marker)

        @Suppress("unused") // used for debugging
        fun dump(): String = this@AioValueWorker.dump()

        fun item(itemId: AioValueId): AioItem =
            values[itemId] as AioItem

        inline fun <reified T> markerVal(item: AioValueId, marker: AioMarker): T? =
            getMarkerValue(item, marker) as T?

        fun getMarkerValue(item: AioValueId, marker: AioMarker) =
            values[item]?.let { item ->
                item as AioItem
                item.markersOrNull?.get(marker)?.let { markerValue ->
                    values[markerValue]
                }
            }

        fun getContainingList(childId: AioValueId, childListMarker: AioMarker, topListMarker: AioMarker): AmvItemIdList? {
            val parentId = item(childId).parentId

            val original: AmvItemIdList?

            if (parentId == null) {
                original = queryByMarker(topListMarker).firstOrNull() as? AmvItemIdList
            } else {
                original = markerVal(parentId, childListMarker)
            }

            return original
        }

        fun addTopList(spaceId: AioValueId, listMarker: AioMarker) {
            val original = queryByMarker(listMarker).firstOrNull() as AmvItemIdList?
            val new: AmvItemIdList

            if (original != null) {
                new = original.copy(itemIds = original.itemIds + spaceId)
            } else {
                new = AmvItemIdList(owner = uuid7(), listMarker, listOf(spaceId))
            }

            this += new
        }

        fun addChild(parentId: AioValueId, childId: AioValueId, childListMarker: AioMarker) {
            val original: AmvItemIdList? = markerVal(parentId, childListMarker)

            if (original != null) {
                this += original.copy(itemIds = original.itemIds + childId)
                return
            }

            val new = AmvItemIdList(owner = parentId, childListMarker, listOf(childId))

            val parent = item(parentId)

            val markers = parent.markersOrNull?.toMutableMap() ?: mutableMapOf<AioMarker, AioValueId?>()

            markers[childListMarker] = new.uuid

            this += new
            this += parent.copy(markersOrNull = markers)
        }

        fun moveUp(childId: AioValueId, childListMarker: AioMarker, topListMarker: AioMarker) {

            val original = getContainingList(childId, childListMarker, topListMarker) ?: return

            val originalList = original.itemIds.toMutableList()
            val index = originalList.indexOf(childId)
            if (index < 1) return

            val newList = originalList.toMutableList()
            newList[index] = newList[index - 1]
            newList[index - 1] = childId

            this += original.copy(itemIds = newList)
        }

        fun moveDown(childId: AioValueId, childListMarker: AioMarker, topListMarker: AioMarker) {

            val original = getContainingList(childId, childListMarker, topListMarker) ?: return

            val originalList = original.itemIds.toMutableList()
            val index = originalList.indexOf(childId)
            if (index >= originalList.lastIndex) return

            val newList = originalList.toMutableList()
            newList[index] = newList[index + 1]
            newList[index + 1] = childId

            this += original.copy(itemIds = newList)
        }
    }
}