package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.api.adatCompanionOf
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.internal.origin.AutoInstanceBuilder
import `fun`.adaptive.auto.internal.origin.AutoItem
import `fun`.adaptive.auto.internal.producer.AutoItemProducer
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer

/**
 * Create an **endpoint** and connect it to [peer] with **direct** connection.
 *
 * This is a producer intended for use in an Adaptive fragment.
 *
 * @param    listener       An optional listener to get auto events.
 * @param    binding        Set by the compiler plugin, ignore it.
 * @param    trace         Log trace information.
 */
@Producer
fun <A : AdatClass> autoItem(
    peer: AutoInstance<*, *, A, A>,
    listener: AutoItemListener<A>? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    trace: Boolean = false,
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoItemProducer(binding, { peer.connectInfo(AutoConnectionType.Direct) }, listener, trace)

    binding.targetFragment.addProducer(store)

    return null
}

/**
 * Create an **endpoint** and connect it to the peer with [AutoApi] using **service** connection.
 *
 * This is a producer intended for use in an Adaptive fragment.
 *
 * Uses the transport of the fragment adapter.
 *
 * @param    listener       An optional listener to get auto events.
 * @param    binding        Set by the compiler plugin, ignore it.
 * @param    trace          Log trace information.
 * @param    infoFun        A function to get the connection info. Typically, this is created by
 *                          a service call.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass> autoItem(
    listener: AutoItemListener<A>? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    trace: Boolean = false,
    infoFun: suspend () -> AutoConnectionInfo<A>,
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoItemProducer(binding, infoFun, listener, trace)

    binding.targetFragment.addProducer(store)

    return null
}

/**
 * Create an **origin controller** with the initial value set to [initialValue].
 *
 * The created controller:
 *
 * - **does not connect** to any other nodes automatically
 * - is **not registered** with any worker
 * - can have **only direct** connections
 *
 * @param    initialValue   The value of the auto instance.
 * @param    listener       An optional listener to get auto events.
 * @param    trace          Log trace information.
 */
@Suppress("UNCHECKED_CAST")
fun <A : AdatClass> autoItem(
    initialValue: A,
    listener: AutoItemListener<A>? = null,
    trace: Boolean = false,
): AutoItem<PropertyBackend<A>, AdatClassFrontend<A>, A> =

    buildItem(
        origin = true,
        service = false,
        initialValue = initialValue,
        defaultWireFormat = (initialValue.adatCompanion as AdatCompanion<A>).adatWireFormat,
        listener = listener,
        trace = trace
    )

/**
 * Create an **origin controller** with [initialValue] and register
 * it with [worker] for incoming service connections.
 *
 * The created controller:
 *
 * - does not connect to any other nodes automatically
 * - **is registered** with [worker]
 * - may have **service or direct** connections
 *
 * @param    worker         The worker to register this instance with.
 * @param    initialValue   The value of the auto instance.
 * @param    listener       An optional listener to get auto events.
 * @param    trace          Log trace information.
 */
@Suppress("UNCHECKED_CAST")
fun <A : AdatClass> autoItem(
    worker: AutoWorker,
    initialValue: A,
    listener: AutoItemListener<A>? = null,
    trace: Boolean = false,
): AutoItem<PropertyBackend<A>, AdatClassFrontend<A>, A> =

    buildItem(
        origin = true,
        service = true,
        initialValue = initialValue,
        defaultWireFormat = (initialValue.adatCompanion as AdatCompanion<A>).adatWireFormat,
        listener = listener,
        trace = trace,
        worker = worker
    )

/**
 * Create a **node** with no initial value and connect
 * it to another auto node.
 *
 * The created instance:
 *
 * - **initiates a connection** with [infoFunSuspend]
 * - **is registered** with the [worker]
 * - may have **service or direct** connections
 *
 * @param    worker          The worker to register this instance with.
 * @param    listener        An optional listener to get auto events.
 * @param    trace           Log trace information.
 * @param    infoFunSuspend  Called to get the connection info from the peer.
 */
@Suppress("UNCHECKED_CAST")
fun <A : AdatClass> autoItem(
    worker: AutoWorker,
    listener: AutoItemListener<A>? = null,
    trace: Boolean = false,
    infoFunSuspend: InfoFunSuspend<PropertyBackend<A>, AdatClassFrontend<A>, A, A>,
): AutoItem<PropertyBackend<A>, AdatClassFrontend<A>, A> =

    buildItem(
        origin = false,
        service = true,
        infoFunSuspend = infoFunSuspend,
        defaultWireFormat = adatCompanionOf<A>().adatWireFormat,
        listener = listener,
        trace = trace,
        worker = worker
    )

@Suppress("UNCHECKED_CAST")
private fun <A : AdatClass> buildItem(
    origin : Boolean,
    service : Boolean,
    defaultWireFormat : AdatClassWireFormat<A>,
    initialValue: A? = null,
    infoFunSuspend: InfoFunSuspend<PropertyBackend<A>, AdatClassFrontend<A>, A, A>? = null,
    listener: AutoItemListener<A>? = null,
    trace: Boolean,
    worker : AutoWorker? = null,
): AutoItem<PropertyBackend<A>, AdatClassFrontend<A>, A> =

    AutoInstanceBuilder<PropertyBackend<A>, AdatClassFrontend<A>, A, A>(
        origin = origin,
        persistent = false,
        service = service,
        collection = false,
        infoFunSuspend = infoFunSuspend,
        defaultWireFormat = defaultWireFormat,
        itemListener = listener,
        worker = worker,
        trace = trace,
        backendFun = { builder, info, value ->
            PropertyBackend(
                builder.instance,
                null,
                value?.toArray()
            )
        },
        persistenceFun = { builder ->
            AdatClassFrontend(
                builder.instance,
                initialValue,
                collectionFrontend = null
            )
        }
    ).build(
        initialValue
    ) as AutoItem<PropertyBackend<A>, AdatClassFrontend<A>, A>