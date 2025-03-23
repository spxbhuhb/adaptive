package `fun`.adaptive.value

import `fun`.adaptive.adat.encodeToJsonString
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.p04
import `fun`.adaptive.utility.use
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItemIdList
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvMarkerValue
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence
import `fun`.adaptive.wireformat.asPrettyJson
import `fun`.adaptive.wireformat.builtin.PolymorphicWireFormat
import `fun`.adaptive.wireformat.json.JsonWireFormatEncoder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AvValueWorker(
    val persistence: AbstractValuePersistence = NoPersistence()
) : WorkerImpl<AvValueWorker> {

    private val lock = getLock()

    private val operations = Channel<AvValueOperation>(Channel.UNLIMITED)

    private val values = mutableMapOf<AvValueId, AvValue>()

    private val subscriptions = mutableMapOf<AvValueSubscriptionId, AvSubscription>()

    private val valueIdSubscriptions = mutableMapOf<AvValueId, MutableList<AvSubscription>>()

    private val markerSubscriptions = mutableMapOf<AvMarker, MutableList<MarkerSubscriptionEntry>>()

    private val markerIndices = mutableMapOf<AvMarker, MutableSet<AvValueId>>()

    private val trace = false

    @OptIn(ExperimentalCoroutinesApi::class)
    val isIdle: Boolean
        get() = operations.isEmpty

    class MarkerSubscriptionEntry(
        val subscription: AvSubscription,
        val itemOnly: Boolean
    )

    // --------------------------------------------------------------------------------
    // Operations
    // --------------------------------------------------------------------------------

    @ExperimentalCoroutinesApi
    override suspend fun run() {
        if (trace) logger.enableFine() // .also { it.usePrintln = true}

        lock.use {
            if (GlobalRuntimeContext.isServer) {
                check(values.isEmpty()) { "Values are not empty" }
                check(subscriptions.isEmpty()) { "Subscriptions are not empty" }
                check(markerIndices.isEmpty()) { "Marker indices are not empty" }
                check(operations.isEmpty) { "Operations are not empty" }
            }

            persistence.loadValues(values)

            // this will be empty as there are no subscriptions
            val commitSet = mutableSetOf<AvSubscription>()

            values.forEach { (_, value) ->
                index(null, value, commitSet)
            }
        }

        for (operation in operations) {
            lock.use {
                if (trace) logOperation(operation)

                val commitSet = mutableSetOf<AvSubscription>()

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

    private fun logOperation(operation: AvValueOperation) {
        if (operation is AvoTransaction) {
            logger.fine("Transaction:")
            operation.operations.forEach { logger.fine("  ${formatOperation(it)}") }
        } else {
            logger.fine("Operation: ${formatOperation(operation)}")
        }
    }

    private fun formatOperation(operation: AvValueOperation): String =
        if (logger.usePrintln) {
            operation.encodeToJsonString().asPrettyJson
        } else {
            operation.toString()
        }


    private fun add(operation: AvoAdd, commitSet: MutableSet<AvSubscription>) {
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

    private fun update(operation: AvoUpdate, commitSet: MutableSet<AvSubscription>) {
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

    private fun addOrUpdate(operation: AvoAddOrUpdate, commitSet: MutableSet<AvSubscription>) {
        val value = operation.value
        val original = values.put(value.uuid, value)

        persistence.saveValue(value)
        indexAndNotify(original, value, operation, commitSet)
    }

    private fun transaction(transaction: AvoTransaction, commitSet: MutableSet<AvSubscription>) {
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

    private fun compute(operation: AvoComputation<*>, commitSet: MutableSet<AvSubscription>) {
        try {
            val result = operation.computation?.invoke(WorkerComputeContext(commitSet))
            operation.success(result)
        } catch (e: Exception) {
            operation.fail(e)
        }
    }

    private fun indexAndNotify(original: AvValue?, value: AvValue, operation: AvValueOperation, commitSet: MutableSet<AvSubscription>) {
        index(original, value, commitSet)
        notify(value, commitSet)
        operation.success()
    }

    // --------------------------------------------------------------------------------
    // Notification (for remove marker check indexing also)
    // --------------------------------------------------------------------------------

    fun notify(value: AvValue, commitSet: MutableSet<AvSubscription>) {
        valueIdSubscriptions[value.uuid]?.forEach {
            add(it, value, commitSet)
        }

        notifyByMarker(value, commitSet)
    }

    fun notifyByMarker(value: AvValue, commitSet: MutableSet<AvSubscription>) {
        when (value) {
            is AvItem<*> -> value.markersOrNull?.forEach { marker -> notifyByMarker(marker.key, value, commitSet) }
            is AvMarkerValue -> notifyByMarker(value.markerName, value, commitSet)
        }
    }

    fun notifyByMarker(markerName: String, value: AvValue, commitSet: MutableSet<AvSubscription>) {
        markerSubscriptions[markerName]?.forEach {
            it.ifApplicable(value) { add(it.subscription, value, commitSet) }
        }
    }

    private fun add(subscription: AvSubscription, value: AvValue, commitSet: MutableSet<AvSubscription>) {
        try {
            subscription.add(value)
            commitSet.add(subscription)
        } catch (e: Exception) {
            logger.warning(e)
            commitSet.remove(subscription)
            unsubscribe(subscription.uuid)
        }
    }

    fun commit(commitSet: MutableSet<AvSubscription>) {
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

    private fun index(original: AvValue?, value: AvValue, commitSet: MutableSet<AvSubscription>) {
        when (value) {

            is AvItem<*> -> {
                index(
                    value,
                    (original as? AvItem<*>)?.markers?.keys ?: emptySet(),
                    value.markers.keys,
                    commitSet
                )
            }

            is AvMarkerValue -> {
                markerIndices.getOrPut(value.markerName) { mutableSetOf() }.add(value.uuid)
            }
        }
    }

    private fun index(
        value: AvValue,
        originalMarkers: Set<String>,
        markers: Set<String>,
        commitSet: MutableSet<AvSubscription>
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
        subscription: AvSubscription,
        valueId: AvValueId,
        marker: AvMarker,
        commitSet: MutableSet<AvSubscription>
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

    private inline fun MarkerSubscriptionEntry.ifApplicable(value: AvValue, block: () -> Unit) {
        if (itemOnly && value !is AvItem<*>) return
        block()
    }

    // --------------------------------------------------------------------------------
    // Subscription management
    // --------------------------------------------------------------------------------

    fun subscribe(subscription: AvSubscription) {
        lock.use {
            unsafeSubscribe(subscription)
        }
    }

    fun subscribe(subscriptions: List<AvSubscription>) {
        lock.use {
            for (subscription in subscriptions) {
                unsafeSubscribe(subscription)
            }
        }
    }

    private fun unsafeSubscribe(subscription: AvSubscription) {
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

    private fun addValueIdSubscription(subscription: AvSubscription, valueId: AvValueId) {
        this.valueIdSubscriptions
            .getOrPut(valueId) { mutableListOf() }
            .add(subscription)

        values[valueId]?.let {
            subscription.add(it)
        }
    }

    private fun addMarkerSubscription(subscription: AvSubscription, marker: AvMarker, itemOnly: Boolean) {
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

    fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        lock.use {
            unsafeUnsubscribe(subscriptionId)
        }
    }

    private fun unsafeUnsubscribe(subscriptionId: AvValueSubscriptionId) {
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

    fun subscriptionCount(valueId: AvValueId): Int =
        lock.use {
            valueIdSubscriptions[valueId]?.size ?: 0
        }

    // --------------------------------------------------------------------------------
    // Query
    // --------------------------------------------------------------------------------

    operator fun get(valueId: AvValueId): AvValue? =
        lock.use {
            values[valueId]
        }

    fun item(valueId: AvValueId): AvItem<*> =
        get(valueId) as AvItem<*>

    inline fun <reified T> markerVal(itemId: AvValueId, marker: AvMarker) =
        getMarkerValue(itemId, marker) as T

    inline fun <reified T> markerValOrNull(itemId: AvValueId, marker: AvMarker) =
        getMarkerValue(itemId, marker) as T?

    fun getMarkerValue(itemId: AvValueId, marker: AvMarker): AvMarkerValue? =
        lock.use {
            unsafeGetMarkerValue(itemId, marker)
        }

    private fun unsafeGetMarkerValue(valueId: AvValueId, marker: AvMarker): AvMarkerValue? =
        values[valueId]?.let { item ->
            item as AvItem<*>
            item.markersOrNull?.get(marker)?.let { markerValue ->
                values[markerValue]
            }
        } as AvMarkerValue?

    fun query(filterFun: (AvValue) -> Boolean): List<AvValue> =
        lock.use {
            values.values.filter(filterFun)
        }

    fun queryByMarker(marker: AvMarker): List<AvValue> =
        lock.use {
            markerIndices[marker]?.mapNotNull { values[it] } ?: emptyList()
        }

    // --------------------------------------------------------------------------------
    // Send operations to the operation queue
    // --------------------------------------------------------------------------------

    fun queueAdd(value: AvValue) {
        check(operations.trySend(AvoAdd(value)).isSuccess) { "Failed to queue add" }
    }

    fun queueAddAll(vararg values: AvValue) =
        queueTransaction(values.map { AvoAdd(it) })

    fun queueUpdate(value: AvValue) {
        check(operations.trySend(AvoUpdate(value)).isSuccess) { "Failed to queue update" }
    }

    fun queueAddOrUpdate(value: AvValue) {
        check(operations.trySend(AvoAddOrUpdate(value)).isSuccess) { "Failed to queue addOrUpdate" }
    }

    fun queueTransaction(operations: List<AvValueOperation>) {
        check(this.operations.trySend(AvoTransaction(operations)).isSuccess) { "Failed to queue transaction" }
    }

    fun queueOperation(operation: AvValueOperation) {
        check(this.operations.trySend(operation).isSuccess) { "Failed to queue operation" }
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
    suspend fun <T> execute(timeout: Duration = 5.seconds, computeFun: AvComputeFun<T>): T {
        val channel = Channel<Any>()

        val op = AvoComputation<T>().also {
            it.channel = channel
            it.computation = computeFun
        }

        check(this.operations.trySend(op).isSuccess) { "Failed to queue execute" }

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

    /**
     * Executes an update on the value with id [valueId]:
     *
     * - fetches the value
     * - calls [updateFun]
     * - replaces the original value with the return value of [updateFun]
     *
     * Behaviour:
     *
     * - calls [execute], all behaviour of [execute] applies
     */
    suspend inline fun <reified T : AvValue> update(
        valueId: AvValueId,
        timeout: Duration = 5.seconds,
        noinline updateFun: (T) -> T
    ) {
        executeUpdate(valueId, timeout, T::class, updateFun)
    }

    /**
     * Executes an update on an item with id [valueId]:
     *
     * - fetches the value
     * - calls [updateFun]
     * - replaces the original value with the return value of [updateFun]
     *
     * Behaviour:
     *
     * - calls [execute], all behaviour of [execute] applies
     */
    suspend inline fun updateItem(
        valueId: AvValueId,
        timeout: Duration = 5.seconds,
        noinline updateFun: (AvItem<*>) -> AvItem<*>
    ) {
        executeUpdate(valueId, timeout, AvItem::class, updateFun)
    }

    suspend fun <T : AvValue> executeUpdate(
        valueId: AvValueId,
        timeout: Duration,
        kClass: KClass<T>,
        updateFun: (T) -> T
    ) {
        execute(timeout) {
            val value = checkNotNull(values[valueId]) { "Value with id $valueId does not exist" }
            check(kClass.isInstance(value)) { "Value with id $valueId is not of type ${kClass.simpleName}" }

            @Suppress("UNCHECKED_CAST")
            this += updateFun(value as T)
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
        val commitSet: MutableSet<AvSubscription>
    ) {

        operator fun plusAssign(value: AvValue) {
            addOrUpdate(AvoAddOrUpdate(value), commitSet)
        }

        operator fun get(valueId: AvValueId): AvValue? =
            this@AvValueWorker[valueId]

        fun nextFriendlyId(marker: AvMarker, prefix: String): String {
            var max = 0

            forEachItemByMarker(marker) { value ->
                val i = value.friendlyId.removePrefix(prefix).toIntOrNull()
                if (i != null && i > max) max = i
            }

            return "$prefix${(max + 1).p04}"
        }

        fun forEachByMarker(marker: AvMarker, block: (AvValue) -> Unit) {
            markerIndices[marker]?.forEach { block(values[it] !!) }
        }

        fun forEachItemByMarker(marker: AvMarker, block: (AvItem<*>) -> Unit) {
            markerIndices[marker]?.forEach {
                val value = values[it] as? AvItem<*>
                if (value != null) block(value)
            }
        }

        fun queryByMarker(marker: AvMarker): List<AvValue> =
            this@AvValueWorker.queryByMarker(marker)

        @Suppress("unused") // used for debugging
        fun dump(): String = this@AvValueWorker.dump()

        fun item(itemId: AvValueId): AvItem<*> =
            values[itemId] as AvItem<*>

        fun itemOrNull(itemId: AvValueId?): AvItem<*>? =
            itemId?.let { safeItemId -> values[safeItemId]?.let { it as AvItem<*> } }

        fun valueOrNull(valueId: AvValueId?): AvValue? =
            valueId?.let { values[valueId] }

        fun itemIdsOrNull(itemListValeId: AvValueId?): List<AvValueId>? =
            (valueOrNull(itemListValeId) as? AvItemIdList)?.itemIds

        fun safeItemIds(itemListValeId: AvValueId?): List<AvValueId> =
            itemIdsOrNull(itemListValeId) ?: emptyList()

        inline fun <reified T> markerVal(itemId: AvValueId, marker: AvMarker) =
            getMarkerValue(itemId, marker) as T

        inline fun <reified T> markerValOrNull(itemId: AvValueId, marker: AvMarker) =
            getMarkerValue(itemId, marker) as T?

        fun getContainingList(
            childId: AvValueId,
            childListMarker: AvMarker, topListMarker: AvMarker
        ): AvItemIdList? {
            val parentId = item(childId).parentId

            val original: AvItemIdList?

            if (parentId == null) {
                original = queryByMarker(topListMarker).firstOrNull() as? AvItemIdList
            } else {
                original = markerVal(parentId, childListMarker)
            }

            return original
        }

        fun addTopList(
            spaceId: AvValueId,
            listMarker: AvMarker
        ) {
            val original = queryByMarker(listMarker).firstOrNull() as AvItemIdList?
            val new: AvItemIdList

            if (original != null) {
                new = original.copy(itemIds = original.itemIds + spaceId)
            } else {
                new = AvItemIdList(parentId = uuid7(), listMarker, listOf(spaceId))
            }

            this += new
        }

        fun addChild(
            parentId: AvValueId,
            childId: AvValueId,
            childListMarker: AvMarker
        ) {
            val original: AvItemIdList? = markerVal(parentId, childListMarker)

            if (original != null) {
                this += original.copy(itemIds = original.itemIds + childId)
                return
            }

            val new = AvItemIdList(parentId = parentId, childListMarker, listOf(childId))

            val parent = item(parentId)

            val markers = parent.toMutableMarkers()

            markers[childListMarker] = new.uuid

            this += new
            this += parent.copy(markersOrNull = markers)
        }

        fun removeChild(
            parentId: AvValueId,
            childId: AvValueId,
            childListMarker: AvMarker
        ) {
            val original: AvItemIdList? = markerVal(parentId, childListMarker)

            if (original != null) {
                this += original.copy(itemIds = original.itemIds - childId)
            }
        }

        fun moveUp(
            childId: AvValueId,
            childListMarker: AvMarker,
            topListMarker: AvMarker
        ) {

            val original = getContainingList(childId, childListMarker, topListMarker) ?: return

            val originalList = original.itemIds.toMutableList()
            val index = originalList.indexOf(childId)
            if (index < 1) return

            val newList = originalList.toMutableList()
            newList[index] = newList[index - 1]
            newList[index - 1] = childId

            this += original.copy(itemIds = newList)
        }

        fun moveDown(
            childId: AvValueId,
            childListMarker: AvMarker,
            topListMarker: AvMarker
        ) {

            val original = getContainingList(childId, childListMarker, topListMarker) ?: return

            val originalList = original.itemIds.toMutableList()
            val index = originalList.indexOf(childId)
            if (index >= originalList.lastIndex) return

            val newList = originalList.toMutableList()
            newList[index] = newList[index + 1]
            newList[index + 1] = childId

            this += original.copy(itemIds = newList)
        }

        fun addRef(
            itemId: AvValueId,
            refMarker: AvMarker,
            refValueId: AvValueId,
            listMarker: AvMarker
        ) {
            val item = item(itemId)
            val markers = item.toMutableMarkers()

            val ref = markers[refMarker]

            if (ref == refValueId) return

            if (ref != null) {
                removeChild(ref, itemId, listMarker)
            }

            markers[refMarker] = refValueId

            this += item.copy(markersOrNull = markers)

            addChild(refValueId, itemId, listMarker)
        }

        fun removeRef(
            itemId: AvValueId,
            refMarker: AvMarker,
            listMarker: AvMarker
        ) {
            val item = item(itemId)
            val markers = item.toMutableMarkers()
            if (markers[refMarker] == null) return

            this += item.copy(markersOrNull = markers)

            val ref = markers.remove(refMarker) ?: return

            removeChild(itemId, ref, listMarker)
        }
    }

}