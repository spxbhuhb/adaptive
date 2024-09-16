package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.internal.producer.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.service.ServiceContext

/**
 * Connect to peers with [AutoApi] and produce an instance that is
 * automatically synchronized between peers.
 *
 * - Backend: PropertyBackend
 * - Frontend: AdatClassFrontend
 *
 * Property changes (on any peer) generate a new instance (on all peers).
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoInstance] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * **This function is NOT thread safe.**
 *
 * @param    onChange       Called after a new instance is generated, but before the
 *                          state of the fragment is updated.
 * @param    binding        Set by the compiler plugin, ignore it.
 * @param    connect        A function to get the connection info. Typically, this is created by
 *                          a service call.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass> autoInstance(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    trace: Boolean = false,
    connect: suspend () -> AutoConnectInfo<A>
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoInstance(binding, connect, onChange, trace)

    binding.targetFragment.addProducer(store)

    return null
}


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
 * @param    onChange       Called after a new instance is generated (that is,
 *                          after a property changes).
 *
 * @return   An [OriginBase] for this auto instance. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass> autoInstance(
    worker: AutoWorker,
    initialValue: A,
    serviceContext: ServiceContext? = null,
    handle : AutoHandle = AutoHandle(),
    trace: Boolean = false,
    onChange: ((newValue: A) -> Unit)? = null
): OriginBase<PropertyBackend, AdatClassFrontend<A>, A> {

    val companion = initialValue.adatCompanion

    @Suppress("UNCHECKED_CAST")
    val origin = OriginBase<PropertyBackend, AdatClassFrontend<A>, A>(
        worker,
        handle,
        serviceContext,
        companion.adatWireFormat,
        trace
    ) {
        backend = PropertyBackend(
            context,
            LamportTimestamp(1, 1),
            null,
            initialValue.toArray()
        )

        frontend = AdatClassFrontend(
            backend,
            companion.adatWireFormat as AdatClassWireFormat<A>,
            initialValue,
            null, null
        ) {
            it.value?.let { value ->
                value.validateForContext()
                onChange?.invoke(value)
            }
        }
    }

    return origin

}