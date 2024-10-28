package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.internal.origin.OriginInstanceBase
import `fun`.adaptive.auto.internal.producer.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.service.ServiceContext

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
fun <A : AdatClass> autoInstance(
    peer: OriginBase<*, *, A, A>,
    listener: AutoInstanceListener<A>? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    trace: Boolean = false
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoInstance(binding, { peer.connectInfo(AutoConnectionType.Direct) }, listener, trace)

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
 * @param    connect        A function to get the connection info. Typically, this is created by
 *                          a service call.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass> autoInstance(
    listener : AutoInstanceListener<A>? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    trace: Boolean = false,
    connect: suspend () -> AutoConnectionInfo<A>
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoInstance(binding, connect, listener, trace)

    binding.targetFragment.addProducer(store)

    return null
}

/**
 * Create a **controller** with the initial value set to [initialValue].
 * The created auto instance is **NOT**  registered, it can have only **direct**
 * connections.
 *
 * @param    initialValue   The value of the auto instance.
 * @param    listener       An optional listener to get auto events.
 * @param    trace          Log trace information.
 */
@Suppress("UNCHECKED_CAST")
fun <A : AdatClass> autoInstance(
    initialValue: A,
    listener: AutoInstanceListener<A>? = null,
    trace: Boolean = false
): OriginBase<PropertyBackend<A>, AdatClassFrontend<A>, A, A> =
    autoInstance(
        worker = null,
        companion = initialValue.adatCompanion as AdatCompanion<A>,
        initialValue = initialValue,
        listener = listener,
        register = false,
        trace = trace
    )

/**
 * Registers a copy of [initialValue] as an Auto instance with [worker].
 *
 * After registration peers can use [autoInstance] to connect to the registered
 * instance. To get the connection info needed for the [autoInstance]
 * use the `connectInfo` function of the returned frontend.
 *
 * Property changes (on any peer) generate a new instance (on all peers).
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The instance is **NOT** thread safe.
 *
 * @return   An [OriginBase] for this auto instance. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass> autoInstance(
    worker: AutoWorker?,
    companion: AdatCompanion<A>,
    initialValue: A? = null,
    listener : AutoInstanceListener<A>? = null,
    serviceContext: ServiceContext? = null,
    handle : AutoHandle = AutoHandle(),
    itemId: ItemId = handle.itemId ?: LamportTimestamp.CONNECTING,
    register: Boolean = true,
    trace: Boolean = false
): OriginInstanceBase<PropertyBackend<A>, AdatClassFrontend<A>, A> {

    val pItemId : ItemId
    val propertyTimes : List<LamportTimestamp>
    val value: Array<Any?>
    val commit : Boolean

    val size = companion.adatMetadata.properties.size

    when {
        initialValue != null -> {
            pItemId = if (itemId === LamportTimestamp.CONNECTING) LamportTimestamp.ORIGIN else itemId
            value = initialValue.toArray()
            propertyTimes = MutableList(size) { pItemId }
            commit = true
        }

        else -> {
            pItemId = if (itemId === LamportTimestamp.CONNECTING) LamportTimestamp.ORIGIN else itemId
            value = arrayOfNulls<Any?>(size)
            propertyTimes = MutableList(size) { LamportTimestamp.CONNECTING }
            commit = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    val origin = OriginInstanceBase<PropertyBackend<A>, AdatClassFrontend<A>, A>(
        worker,
        handle,
        serviceContext,
        companion.adatWireFormat,
        register = register,
        trace = trace
    ) {

        if (listener != null) context.addListener(listener)

        backend = PropertyBackend(
            context,
            pItemId,
            null,
            value,
            propertyTimes
        )

        frontend = AdatClassFrontend(
            backend,
            companion.adatWireFormat,
            initialValue,
            pItemId,
            null
        )
    }

    return origin

}