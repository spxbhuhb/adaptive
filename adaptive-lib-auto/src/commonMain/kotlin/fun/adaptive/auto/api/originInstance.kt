package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.CleanupHandler

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
fun <A : AdatClass<A>> originInstance(
    worker: AutoWorker,
    initialValue: A,
    serviceContext: ServiceContext? = null,
    trace: Boolean = false,
    onChange: ((newValue: A) -> Unit)? = null
): OriginBase<PropertyBackend, AdatClassFrontend<A>> {

    val companion = initialValue.adatCompanion

    val origin = OriginBase<PropertyBackend, AdatClassFrontend<A>>(
        worker,
        serviceContext,
        companion.adatMetadata,
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
            companion.adatWireFormat,
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