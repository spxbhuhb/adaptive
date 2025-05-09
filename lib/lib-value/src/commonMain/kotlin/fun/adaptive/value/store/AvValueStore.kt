package `fun`.adaptive.value.store

import `fun`.adaptive.adat.encodeToJsonString
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue
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
import kotlin.time.measureTime

open class AvValueStore(
    val persistence: AbstractValuePersistence = NoPersistence(),
    private val logger: AdaptiveLogger,
    var trace: Boolean = false
) {

    private val lock = getLock()

    private val operations = Channel<AvValueOperation>(Channel.UNLIMITED)

    private val values = mutableMapOf<AvValueId, AvValue<*>>()

    private val subscriptions = mutableMapOf<AvValueSubscriptionId, AvSubscription>()

    private val valueIdSubscriptions = mutableMapOf<AvValueId, MutableList<AvSubscription>>()

    private val markerSubscriptions = mutableMapOf<AvMarker, MutableList<MarkerSubscriptionEntry>>()

    private val markerIndices = mutableMapOf<AvMarker, MutableSet<AvValueId>>()

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
    suspend fun run() {
        if (trace) logger.enableFine() // .also { it.usePrintln = true}

        lock.use {
            if (GlobalRuntimeContext.isServer) {
                check(values.isEmpty()) { "Values are not empty" }
                check(subscriptions.isEmpty()) { "Subscriptions are not empty" }
                check(markerIndices.isEmpty()) { "Marker indices are not empty" }
                check(operations.isEmpty) { "Operations are not empty" }
            }

            measureTime {
                persistence.loadValues(values)
            }.also {
                logger.info("loaded ${values.size} values in $it")
            }

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


    internal fun add(operation: AvoAdd, commitSet: MutableSet<AvSubscription>) {
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

    internal fun update(operation: AvoUpdate, commitSet: MutableSet<AvSubscription>) {
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

    internal fun addOrUpdate(operation: AvoAddOrUpdate, commitSet: MutableSet<AvSubscription>) {
        val value = operation.value
        val original = values.put(value.uuid, value)

        persistence.saveValue(value)
        indexAndNotify(original, value, operation, commitSet)
    }

    internal fun transaction(transaction: AvoTransaction, commitSet: MutableSet<AvSubscription>) {
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

    internal fun compute(operation: AvoComputation<*>, commitSet: MutableSet<AvSubscription>) {
        try {
            val result = operation.computation?.invoke(AvComputeContext(this, commitSet))
            operation.success(result)
        } catch (e: Exception) {
            operation.fail(e)
        }
    }

    private fun indexAndNotify(original: AvValue<*>?, value: AvValue<*>, operation: AvValueOperation, commitSet: MutableSet<AvSubscription>) {
        index(original, value, commitSet)
        notify(value, commitSet)
        operation.success()
    }

    // --------------------------------------------------------------------------------
    // Notification (for remove marker check indexing also)
    // --------------------------------------------------------------------------------

    fun notify(value: AvValue<*>, commitSet: MutableSet<AvSubscription>) {
        valueIdSubscriptions[value.uuid]?.forEach {
            add(it, value, commitSet)
        }

        notifyByMarker(value, commitSet)
    }

    fun notifyByMarker(value: AvValue<*>, commitSet: MutableSet<AvSubscription>) {
        if (value.type == AvRefListType) {
            notifyByMarker(value.name, value, commitSet)
        } else {
            value.markersOrNull?.forEach { marker -> notifyByMarker(marker, value, commitSet) }
        }
    }

    fun notifyByMarker(markerName: String, value: AvValue<*>, commitSet: MutableSet<AvSubscription>) {
        markerSubscriptions[markerName]?.forEach {
            it.ifApplicable(value) { add(it.subscription, value, commitSet) }
        }
    }

    private fun add(subscription: AvSubscription, value: AvValue<*>, commitSet: MutableSet<AvSubscription>) {
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

    private fun index(original: AvValue<*>?, value: AvValue<*>, commitSet: MutableSet<AvSubscription>) {
        if (value.type == AvRefListType) {
            markerIndices.getOrPut(value.name) { mutableSetOf() }.add(value.uuid)
        } else {
            index(
                value,
                original?.markers ?: emptySet(),
                value.markers,
                commitSet
            )
        }
    }

    private fun index(
        value: AvValue<*>,
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

    private inline fun MarkerSubscriptionEntry.ifApplicable(value: AvValue<*>, block: () -> Unit) {
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
        subscription.store = this

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

            subscription.store = null
        }
    }

    fun subscriptionCount(valueId: AvValueId): Int =
        lock.use {
            valueIdSubscriptions[valueId]?.size ?: 0
        }

    // --------------------------------------------------------------------------------
    // Send operations to the operation queue
    // --------------------------------------------------------------------------------

    fun queue(operation: AvValueOperation) {
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
        val channel = Channel<Any?>(Channel.BUFFERED)

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

    suspend fun <T : Any> executeUpdate(
        valueId: AvValueId,
        timeout: Duration,
        kClass: KClass<T>,
        updateFun: (AvValue<T>) -> AvValue<T>
    ) {
        execute(timeout) {
            val value = checkNotNull(values[valueId]) { "Value with id $valueId does not exist" }
            check(kClass.isInstance(value)) { "Value with id $valueId is not of type ${kClass.simpleName}" }

            @Suppress("UNCHECKED_CAST")
            this += updateFun(value as AvValue<T>)
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
    // Operation support
    // --------------------------------------------------------------------------------

    // These are used by computations and have NO LOCK PROTECTION.
    // DO NOT call any of these from any context that is protected by the store lock.

    internal fun unsafeGet(valueId: AvValueId): AvValue<*> =
        checkNotNull(values[valueId]) { "value with id $valueId does not exist" }

    internal fun unsafeGetOrNull(valueId: AvValueId?): AvValue<*>? =
        valueId?.let { values[valueId] }

    internal fun unsafeRefOrNull(valueId: AvValueId, refName: AvRefLabel): AvValue<*>? =
        values[valueId]?.let { unsafeRefOrNull(it, refName) }

    internal fun unsafeRefOrNull(value: AvValue<*>, refName: AvRefLabel): AvValue<*>? =
        value.refsOrNull?.get(refName)?.let { values[it] }

    internal fun unsafeQueryByMarker(marker: AvMarker): List<AvValue<*>> =
        markerIndices[marker]?.mapNotNull { values[it] } ?: emptyList()

    internal fun unsafeForEachByMarker(marker: AvMarker, block: (AvValue<*>) -> Unit) {
        markerIndices[marker]?.forEach { block(values[it] !!) }
    }

    internal fun unsafeForEachItemByMarker(marker: AvMarker, block: (AvValue<*>) -> Unit) {
        markerIndices[marker]?.forEach {
            val value = values[it]
            if (value != null) block(value)
        }
    }

    // --------------------------------------------------------------------------------
    // Query
    // --------------------------------------------------------------------------------

    fun get(valueId: AvValueId): AvValue<*> =
        getOrNull(valueId) ?: throw NoSuchElementException("Value with id $valueId does not exist.")

    fun getOrNull(valueId: AvValueId): AvValue<*>? =
        lock.use {
            unsafeGetOrNull(valueId)
        }

    fun ref(valueId: AvValueId, refLabel: AvRefLabel): AvValue<*> =
        refOrNull(valueId, refLabel) ?: throw NoSuchElementException("Reference with label $refLabel not found for value ${valueId}.")

    fun refOrNull(valueId: AvValueId, refLabel: AvRefLabel): AvValue<*>? =
        lock.use {
            unsafeRefOrNull(unsafeGet(valueId), refLabel)
        }

    fun AvValue<*>.ref(refLabel: AvRefLabel): AvValue<*> =
        refOrNull(refLabel) ?: throw NoSuchElementException("Reference with label $refLabel not found for value ${this.uuid}.")

    fun AvValue<*>.refOrNull(refLabel: AvRefLabel): AvValue<*>? =
        lock.use {
            unsafeRefOrNull(this, refLabel)
        }

    fun refList(valueId: AvValueId, refName: AvRefLabel): List<AvValue<*>> {
        return lock.use {
            val value = unsafeGet(valueId)
            val refListId = value.refsOrNull?.get(refName) ?: return emptyList()
            val refList = values[refListId]

            checkNotNull(refList) { "Value with id $refListId does not exist (referenced by $valueId through $refName)" }
            check(refList.type == AvRefListType) { "Value with id $refListId is not a ref list" }

            @Suppress("UNCHECKED_CAST") // we check for refList type above
            val spec = refList.spec as List<AvValueId>

            spec.map {
                checkNotNull(values[it]) { "Value with id $it does not exist, referring value: $valueId, refName: $refName, refList: $refListId" }
            }
        }
    }

    fun query(filterFun: (AvValue<*>) -> Boolean): List<AvValue<*>> =
        lock.use {
            values.values.filter(filterFun)
        }

    fun queryByMarker(marker: AvMarker): List<AvValue<*>> =
        lock.use {
            markerIndices[marker]?.mapNotNull { values[it] } ?: emptyList()
        }

}