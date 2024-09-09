package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.producer.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer

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
    connect: suspend () -> AutoConnectInfo
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoInstance(binding, connect, onChange, trace)

    binding.targetFragment.addProducer(store)

    return null
}