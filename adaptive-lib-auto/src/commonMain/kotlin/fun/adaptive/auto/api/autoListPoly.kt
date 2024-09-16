package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.internal.producer.AutoListPoly
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.service.ServiceContext

/**
 * Connect to peers with [AutoApi] and produce a list that is
 * automatically synchronized between peers.
 *
 * - Adding/removing items from the list generate a new list instance (on all peers).
 * - Property changes (on any peer) generate a new list instance (on all peers).
 * - Property changes keep the non-affected instances.
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoInstance] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    onListCommit       Called after the structure of the list has been changed (add/remove), but before the
 *                              state of the fragment is updated.
 * @param    onItemCommit       Called when a property of a list item has been changed, but before the
 *  *                           state of the fragment is updated.
 * @param    binding            Set by the compiler plugin, ignore it.
 * @param    connect            A function to get the connection info. Typically, this is created by
 *                              a service call.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass> autoListPoly(
    defaultWireFormat: AdatClassWireFormat<*>? = null,
    onListCommit: ((newValue: List<A>) -> Unit)? = null,
    onItemCommit: ((item: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<List<A>>? = null,
    trace: Boolean = false,
    connect: suspend () -> AutoConnectInfo<List<A>>
): List<A>? {
    checkNotNull(binding)

    val store = AutoListPoly(binding, connect, defaultWireFormat, onListCommit, onItemCommit, trace)

    binding.targetFragment.addProducer(store)

    return null
}

/**
 * Registers a polymorphic Auto list with [worker].
 *
 * After registration peers can use [autoListPoly] to connect to the registered
 * list. To get the connection info needed for [autoListPoly] use the `connectInfo`
 * function of the returned frontend.
 *
 * - Adding/removing items from the list generate a new list instance (on all peers).
 * - Property changes (on any peer) generate a new list instance (on all peers).
 * - Property changes keep the non-affected instances.
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoListPoly] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    onListCommit       Called after the structure of the list has been changed (add/remove), but before the
 *                              state of the fragment is updated.
 * @param    onItemCommit       Called when a property of a list item has been changed, but before the
 *                            state of the fragment is updated.
 *
 * @return   An [OriginBase] for this polymorphic auto list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass> autoListPoly(
    worker: AutoWorker,
    defaultWireFormat: AdatClassWireFormat<*>? = null,
    serviceContext: ServiceContext? = null,
    handle: AutoHandle = AutoHandle(),
    trace: Boolean = false,
    onListCommit: ((newValue: List<A>) -> Unit)? = null,
    onItemCommit: ((newValue: List<A>, item: A) -> Unit)? = null
): OriginBase<SetBackend, AdatClassListFrontend<A>, List<A>> {

    return OriginBase(
        worker,
        handle,
        serviceContext,
        defaultWireFormat,
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