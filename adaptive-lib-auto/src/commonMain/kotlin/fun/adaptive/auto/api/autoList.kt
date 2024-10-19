package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.internal.origin.OriginListBase
import `fun`.adaptive.auto.internal.producer.AutoList
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
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
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    binding            Set by the compiler plugin, ignore it.
 * @param    connect            A function to get the connection info. Typically, this is created by
 *                              a service call.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass> autoList(
    companion: AdatCompanion<A>,
    peer: OriginBase<*, *, List<A>, A>? = null,
    listener : AutoCollectionListener<A>? = null,
    binding: AdaptiveStateVariableBinding<List<A>>? = null,
    trace: Boolean = false,
    connect: suspend () -> AutoConnectionInfo<List<A>>?
): List<A>? {
    checkNotNull(binding)

    val store = AutoList(binding, connect, companion.adatWireFormat, listener, peer, trace)

    binding.targetFragment.addProducer(store)

    return null
}

/**
 * Connect to peers with [AutoApi] and produce a list that is
 * automatically synchronized between peers.
 *
 * - Adding/removing items from the list generate a new list instance (on all peers).
 * - Property changes (on any peer) generate a new list instance (on all peers).
 * - Property changes keep the non-affected instances.
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    binding            Set by the compiler plugin, ignore it.
 * @param    connect            A function to get the connection info. Typically, this is created by
 *                              a service call.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass> autoList(
    peer: OriginBase<*, *, List<A>, A>? = null,
    defaultWireFormat: AdatClassWireFormat<*>? = null,
    listener : AutoCollectionListener<A>? = null,
    binding: AdaptiveStateVariableBinding<List<A>>? = null,
    trace: Boolean = false,
    connect: suspend () -> AutoConnectionInfo<List<A>>
): List<A>? {
    checkNotNull(binding)

    val store = AutoList(binding, connect, defaultWireFormat, listener, peer, trace)

    binding.targetFragment.addProducer(store)

    return null
}

/**
 * Creates an Auto List.
 *
 * When [register] is true, register the list with [worker].
 *
 * When [register] is false, use [OriginBase.connectDirect] to create a direct
 * connection.
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
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    worker             Origins that support peer connections must specify pass an [AutoWorker] in this
 *                              parameter. Standalone origins may pass `null`.
 *
 * @return   An [OriginBase] for this auto list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass> autoList(
    worker: AutoWorker,
    defaultWireFormat: AdatClassWireFormat<*>? = null,
    listener : AutoCollectionListener<A>? = null,
    serviceContext: ServiceContext? = null,
    handle: AutoHandle = AutoHandle(),
    register: Boolean = true,
    trace: Boolean = false
): ListBase<A> {

    return OriginListBase(
        worker,
        handle,
        serviceContext,
        defaultWireFormat,
        trace,
        register
    ) {
        if (listener != null) context.addListener(listener)
        backend = SetBackend(context)
        frontend = AdatClassListFrontend(backend)
    }

}


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
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    worker             Origins that support peer connections must specify pass an [AutoWorker] in this
 *                              parameter. Standalone origins may pass `null`.
 *
 * @return   An [OriginBase] for this auto list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass> autoList(
    worker: AutoWorker,
    companion: AdatCompanion<A>,
    listener : AutoCollectionListener<A>? = null,
    serviceContext: ServiceContext? = null,
    handle: AutoHandle = AutoHandle(),
    initialValues: List<A>? = null,
    trace: Boolean = false
): ListBase<A> {

    return OriginListBase(
        worker,
        handle,
        serviceContext,
        companion.adatWireFormat,
        trace
    ) {
        if (listener != null) context.addListener(listener)

        backend = SetBackend(
            context,
            initialValues?.mapIndexed { index, item ->
                PropertyBackend<A>(
                    context,
                    ItemId(1, index + 1L),
                    companion.wireFormatName,
                    item.toArray()
                )
            }?.associateBy { it.itemId }?.toMutableMap()
        )

        frontend = AdatClassListFrontend(backend)

        frontend.commit(initial = true, fromBackend = false)
    }

}