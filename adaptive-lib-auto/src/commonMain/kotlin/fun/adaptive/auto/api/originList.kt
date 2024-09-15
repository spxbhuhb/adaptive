package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.UUID

/**
 * Registers an Auto list with [worker].
 *
 * After registration peers can use [autoList] to connect to the registered
 * list. To get the connection info needed for the [autoList] use the `connectInfo`
 * function of the returned frontend.
 *
 * - Adding/removing items from the list generate a new list instance (on all peers).
 * - Property changes (on any peer) generate a new list instance (on all peers).
 * - Property changes keep the non-affected instances.
 *
 * Each new instance is validated by default, so code that use values
 * produced by [originList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    onListCommit       Called after the structure of the list has been changed (add/remove), but before the
 *                              state of the fragment is updated.
 * @param    onItemCommit       Called when a property of a list item has been changed, but before the
 *                              state of the fragment is updated.
 *
 * @return   An [OriginBase] for this auto list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass> originList(
    worker: AutoWorker,
    companion: AdatCompanion<A>,
    serviceContext: ServiceContext? = null,
    handle : AutoHandle = AutoHandle(UUID(), 1),
    trace: Boolean = false,
    onListCommit: ((newValue: List<A>) -> Unit)? = null,
    onItemCommit: ((newValue: List<A>, item: A) -> Unit)? = null
): OriginBase<SetBackend, AdatClassListFrontend<A>, List<A>> {

    return OriginBase(
        worker,
        handle,
        serviceContext,
        companion.adatMetadata,
        companion.adatWireFormat,
        trace
    ) {

        backend = SetBackend(context)

        frontend = AdatClassListFrontend(
            backend,
            onListCommit = onListCommit,
            onItemCommit = onItemCommit
        )
    }

}