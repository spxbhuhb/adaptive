package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.withSpec
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence
import `fun`.adaptive.value.store.AvValueStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

open class AvValueWorker(
    val persistence: AbstractValuePersistence = NoPersistence(),
    val trace: Boolean = false
) : WorkerImpl<AvValueWorker> {

    val store
        get() = checkNotNull(storeOrNull) { "Store is not initialized" }

    var storeOrNull: AvValueStore? = null

    val isIdle: Boolean
        get() = storeOrNull?.isIdle != false

    override fun mount() {
        storeOrNull = AvValueStore(persistence, logger, trace)
        super.mount()
    }

    override fun unmount() {
        storeOrNull?.close()
        super.unmount()
    }

    @ExperimentalCoroutinesApi
    override suspend fun run() {
        storeOrNull?.run()
    }

    // --------------------------------------------------------------------------------
    // Queue operations
    // --------------------------------------------------------------------------------

    fun queueAdd(value: AvValue) {
        store.queue(AvoAdd(value))
    }

    fun queueAddAll(vararg values: AvValue) =
        store.queue(AvoTransaction(values.map { AvoAdd(it) }))

    fun queueUpdate(value: AvValue) {
        store.queue(AvoUpdate(value))
    }

    fun queueAddOrUpdate(value: AvValue) {
        store.queue(AvoAddOrUpdate(value))
    }

    fun queueTransaction(operations: List<AvValueOperation>) {
        store.queue(AvoTransaction(operations))
    }

    fun queue(operation: AvValueOperation) {
        storeOrNull?.queue(operation)
    }

    // --------------------------------------------------------------------------------
    // Execute and update
    // --------------------------------------------------------------------------------

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
    suspend fun <T> execute(timeout: Duration = 5.seconds, computeFun: AvComputeFun<T>): T =
        store.execute(timeout, computeFun)

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
        store.executeUpdate(valueId, timeout, T::class, updateFun)
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
        store.executeUpdate(valueId, timeout, AvItem::class, updateFun)
    }

    // --------------------------------------------------------------------------------
    // Subscriptions
    // --------------------------------------------------------------------------------

    fun subscribe(subscription: AvSubscription) {
        store.subscribe(subscription)
    }

    fun subscribe(subscriptions: List<AvSubscription>) {
        store.subscribe(subscriptions)
    }

    fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        store.unsubscribe(subscriptionId)
    }

    // --------------------------------------------------------------------------------
    // Queries
    // --------------------------------------------------------------------------------

    fun subscriptionCount(valueId: AvValueId): Int =
        store.subscriptionCount(valueId)

    operator fun get(valueId: AvValueId): AvValue? =
        store[valueId]

    inline fun <reified T> markerVal(itemId: AvValueId, marker: AvMarker) =
        store.getMarkerValue(itemId, marker) as T

    inline fun <reified T> markerValOrNull(itemId: AvValueId, marker: AvMarker) =
        store.getMarkerValue(itemId, marker) as T?

    fun item(valueId: AvValueId): AvItem<*> =
        get(valueId) as AvItem<*>

    inline fun <reified T> refItem(item: AvItem<*>, refMarker: AvMarker) =
        store.refItem(item, refMarker).withSpec<T>()

    inline fun <reified T> refItemOrNull(item: AvItem<*>, refMarker: AvMarker) =
        store.refItemOrNull(item, refMarker)?.withSpec<T>()

    fun queryByMarker(marker: AvMarker): List<AvValue> =
        store.queryByMarker(marker)

    fun query(filterFun: (AvValue) -> Boolean): List<AvValue> =
        store.query(filterFun)

}