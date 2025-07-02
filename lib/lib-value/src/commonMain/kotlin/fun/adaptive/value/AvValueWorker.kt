package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence
import `fun`.adaptive.value.store.AvValueStore
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

open class AvValueWorker(
    val proxy: Boolean,
    val persistence: AbstractValuePersistence = NoPersistence(),
    val trace: Boolean = false
) : WorkerImpl<AvValueWorker>() {

    val store
        get() = checkNotNull(storeOrNull) { "Store is not initialized" }

    var storeOrNull: AvValueStore? = null

    val isIdle: Boolean
        get() = storeOrNull?.isIdle != false

    override fun mount() {
        logger = getLogger(fragment?.name ?: "AvValueWorker")
        storeOrNull = AvValueStore(persistence, proxy, logger, trace).also { it.load() }
        super.mount()
    }

    override fun unmount() {
        storeOrNull?.close()
        super.unmount()
    }

    override suspend fun run() {
        storeOrNull?.run()
    }

    // --------------------------------------------------------------------------------
    // Queue operations
    // --------------------------------------------------------------------------------

    fun queueAdd(value: AvValue<*>) {
        store.queue(AvoAdd(value))
    }

    fun queueAddAll(vararg values: AvValue<*>) =
        store.queue(AvoTransaction(values.map { AvoAdd(it) }))

    fun queueUpdate(value: AvValue<*>) {
        store.queue(AvoUpdate(value))
    }

    fun queueUpdateAll(vararg values: AvValue<*>) =
        store.queue(AvoTransaction(values.map { AvoUpdate(it) }))

    fun queueAddOrUpdate(value: AvValue<*>) {
        store.queue(AvoAddOrUpdate(value))
    }

    fun queueRemove(value: AvValue<*>) {
        store.queue(AvoRemove(value.uuid))
    }

    fun queueRemove(valueId: AvValueId) {
        store.queue(AvoRemove(valueId))
    }

    fun queueTransaction(operations: List<AvValueOperation>) {
        store.queue(AvoTransaction(operations))
    }

    fun <T> queue(computeFun: AvComputeFun<T>) {
        store.queue(
            AvoComputation<T>().also {
                it.computation = computeFun
            }
        )
    }

    fun queue(operation: AvValueOperation) {
        storeOrNull?.queue(operation)
    }

    /**
     * Queue the computation for execution.
     */
    operator fun <T> invoke(computeFun: AvComputeFun<T>) {
        queue(computeFun)
    }

    // --------------------------------------------------------------------------------
    // Execute and update
    // --------------------------------------------------------------------------------

    /**
     * Blocks the workers' operation processing when possible and executes
     * [computeFun] while the loop is blocked.
     *
     * Behavior:
     *
     * - no other changes are applied to values while [computeFun] runs
     * - the changes made by [computeFun] are atomic from outside point of view
     * - Throws exception if [computeFun] throws exception.
     * - [computeFun] **MUST BE FAST**, it runs under the value worker lock, stops everything else
     * - rollback **MUST BE HANDLED** by [computeFun], the worker does not guarantee it
     */
    fun <T> executeOutOfBand(computeFun: AvComputeFun<T>) =
        store.executeBlocking(computeFun)

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
     * - rollback **MUST BE HANDLED** by [computeFun], the worker does not guarantee it
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
     * - calls [execute], all behavior of [execute] applies
     */
    suspend inline fun <reified T : Any> update(
        valueId: AvValueId,
        timeout: Duration = 5.seconds,
        noinline updateFun: (AvValue<T>) -> AvValue<T>
    ) {
        store.executeUpdate(valueId, timeout, T::class, updateFun)
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

    fun unsubscribe(subscriptionId: AvSubscriptionId) {
        store.unsubscribe(subscriptionId)
    }

    // --------------------------------------------------------------------------------
    // Queries
    // --------------------------------------------------------------------------------

    fun subscriptionCount(valueId: AvValueId): Int =
        store.subscriptionCount(valueId)

    inline fun <reified SPEC : Any> get(valueId: AvValueId): AvValue<SPEC> =
        store.get(valueId).checkSpec<SPEC>()

    inline fun <reified SPEC : Any> getOrNull(valueId: AvValueId): AvValue<SPEC>? =
        store.getOrNull(valueId)?.checkSpec<SPEC>()

    inline fun <reified SPEC : Any> get(marker: AvMarker): List<AvValue<SPEC>> =
        store.queryByMarker(marker).map { it.checkSpec<SPEC>() }

    inline fun <reified SPEC : Any> first(marker: AvMarker): AvValue<SPEC> =
        store.queryByMarker(marker).first().checkSpec<SPEC>()

    inline fun <reified SPEC : Any> firstOrNull(marker: AvMarker): AvValue<SPEC>? =
        store.queryByMarker(marker).firstOrNull()?.checkSpec<SPEC>()

    inline fun <reified SPEC : Any> firstOrNull(marker: AvMarker, noinline condition : (AvValue<SPEC>) -> Boolean): AvValue<SPEC>? =
        store.firstOrNull(marker, SPEC::class, condition)

    inline fun <reified SPEC : Any> ref(valueId: AvValueId, refLabel: AvRefLabel) =
        store.ref(valueId, refLabel).checkSpec<SPEC>()

    inline fun <reified SPEC : Any> refOrNull(valueId: AvValueId, refMarker: AvMarker) =
        store.refOrNull(valueId, refMarker)?.checkSpec<SPEC>()

    inline fun <reified SPEC : Any> ref(value: AvValue<*>, refLabel: AvRefLabel) =
        store.ref(value, refLabel).checkSpec<SPEC>()

    inline fun <reified SPEC : Any> refOrNull(value: AvValue<*>, refMarker: AvMarker) =
        store.refOrNull(value, refMarker)?.checkSpec<SPEC>()

    inline fun <reified SPEC : Any> refList(value: AvValue<*>, refListMarker: AvMarker): List<AvValue<SPEC>> =
        store.refList(value.uuid, refListMarker).map { it.checkSpec(SPEC::class) }

    inline fun <reified SPEC : Any> refList(valueId: AvValueId, refListMarker: AvMarker): List<AvValue<SPEC>> =
        store.refList(valueId, refListMarker).map { it.checkSpec(SPEC::class) }

    @Deprecated("User get instead.")
    fun queryByMarker(marker: AvMarker): List<AvValue<*>> =
        store.queryByMarker(marker)

    fun query(filterFun: (AvValue<*>) -> Boolean): List<AvValue<*>> =
        store.query(filterFun)

    // --------------------------------------------------------------------------------
    // Authorization
    // --------------------------------------------------------------------------------

    fun ensureBlobCreateAccess(session: ServiceSession, valueId: AvValueId) {

    }

    fun ensureBlobReadAccess(session: ServiceSession, valueId: AvValueId) {

    }

    fun ensureBlobRemoveAccess(session: ServiceSession, valueId: AvValueId) {

    }

    // --------------------------------------------------------------------------------
    // Status operations
    // --------------------------------------------------------------------------------

    /**
     * Adds [status] to the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to add the status to
     * @param status The status to add
     * @param exclusive When true, checks if the status already exists and throws an exception if it does
     *
     * @throws IllegalStateException if exclusive is true and the status already exists on the value
     */
    suspend fun addStatus(valueId: AvValueId, status: AvStatus, exclusive: Boolean = false) {
        execute {
            val value = get<Any>(valueId)
            if (exclusive) {
                check(status !in value.status) { "Status $status is already on value $valueId" }
            }
            this += value.copy(statusOrNull = value.status + status)
        }
    }

    /**
     * Replaces [oldStatus] with [newStatus] on the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to update statuses on
     * @param oldStatus The status to remove
     * @param newStatus The status to add
     * @param exclusive When true, checks if the new status already exists and throws an exception if it does
     *
     * @throws IllegalStateException if exclusive is true and the new status already exists on the value
     */
    suspend fun replaceStatus(valueId: AvValueId, oldStatus: AvStatus, newStatus: AvStatus, exclusive: Boolean = false) {
        execute {
            val value = get<Any>(valueId)
            val status = value.mutableStatus()

            if (exclusive) {
                check(newStatus !in value.status) { "Status $newStatus is already on value $valueId" }
            }

            status.remove(oldStatus)
            status.add(newStatus)

            this += value.copy(statusOrNull = status)
        }
    }

    /**
     * Removes all [oldStatuses] and adds all [newStatuses] on the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to update status on
     * @param oldStatuses The status to remove
     * @param newStatuses The status to add
     * @param exclusive When true, checks if the new status already exists and throws an exception if it does
     *
     * @throws IllegalStateException if exclusive is true and the new status already exists on the value
     */
    suspend fun replaceStatus(valueId: AvValueId, oldStatuses: Collection<AvStatus>, newStatuses: Collection<AvStatus>, exclusive: Boolean = false) {
        execute {
            val value = get<Any>(valueId)
            val statuses = value.mutableStatus()

            for (status in oldStatuses) {
                statuses.remove(status)
            }

            for (status in newStatuses) {
                if (exclusive) {
                    check(status !in statuses) { "Status $status is already on value $valueId" }
                }
                statuses.add(status)
            }

            this += value.copy(statusOrNull = statuses)
        }
    }

    /**
     * Removes [status] from the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to remove the status from
     * @param status The status to remove
     *
     */
    suspend fun removeStatus(valueId: AvValueId, status: String) {
        execute {
            val value = get<Any>(valueId)

            val newStatus = value.mutableStatus()
            newStatus.remove(status)

            this += value.copy(statusOrNull = if (newStatus.isEmpty()) null else newStatus)
        }
    }
    
    // --------------------------------------------------------------------------------
    // Marker operations
    // --------------------------------------------------------------------------------

    /**
     * Adds [marker] to the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to add the marker to
     * @param marker The marker to add
     * @param exclusive When true, checks if the marker already exists and throws an exception if it does
     *
     * @throws IllegalStateException if exclusive is true and the marker already exists on the value
     */
    suspend fun addMarker(valueId: AvValueId, marker: AvMarker, exclusive: Boolean = false) {
        execute {
            val value = get<Any>(valueId)
            if (exclusive) {
                check(marker !in value.markers) { "Marker $marker is already on value $valueId" }
            }
            this += value.copy(markersOrNull = value.markers + marker)
        }
    }

    /**
     * Replaces [oldMarker] with [newMarker] on the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to update markers on
     * @param oldMarker The marker to remove
     * @param newMarker The marker to add
     * @param exclusive When true, checks if the new marker already exists and throws an exception if it does
     *
     * @throws IllegalStateException if exclusive is true and the new marker already exists on the value
     */
    suspend fun replaceMarker(valueId: AvValueId, oldMarker: AvMarker, newMarker: AvMarker, exclusive: Boolean = false) {
        execute {
            val value = get<Any>(valueId)
            val markers = value.mutableMarkers()

            if (exclusive) {
                check(newMarker !in value.markers) { "Marker $newMarker is already on value $valueId" }
            }

            markers.remove(oldMarker)
            markers.add(newMarker)

            this += value.copy(markersOrNull = markers)
        }
    }

    /**
     * Removes all [oldMarkers] and adds all [newMarkers] on the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to update markers on
     * @param oldMarkers The markers to remove
     * @param newMarkers The markers to add
     * @param exclusive When true, checks if the new marker already exists and throws an exception if it does
     *
     * @throws IllegalStateException if exclusive is true and the new marker already exists on the value
     */
    suspend fun replaceMarkers(valueId: AvValueId, oldMarkers: Collection<AvMarker>, newMarkers: Collection<AvMarker>, exclusive: Boolean = false) {
        execute {
            val value = get<Any>(valueId)
            val markers = value.mutableMarkers()

            for (marker in oldMarkers) {
                markers.remove(marker)
            }

            for (marker in newMarkers) {
                if (exclusive) {
                    check(marker !in value.markers) { "Marker $marker is already on value $valueId" }
                }
                markers.add(marker)
            }

            this += value.copy(markersOrNull = markers)
        }
    }

    /**
     * Removes [marker] from the value identified by [valueId].
     *
     * Calls [execute], all behavior of [execute] applies.
     *
     * @param valueId The ID of the value to remove the marker from
     * @param marker The marker to remove
     *
     */
    suspend fun removeMarker(valueId: AvValueId, marker: String) {
        execute {
            val value = get<Any>(valueId)

            val newMarkers = value.mutableMarkers()
            newMarkers.remove(marker)

            this += value.copy(markersOrNull = if (newMarkers.isEmpty()) null else newMarkers)
        }
    }
}