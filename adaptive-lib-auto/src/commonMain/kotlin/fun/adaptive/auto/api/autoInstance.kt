package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.internal.producer.AutoInstance
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer

/**
 * Connect to peers with [AutoApi] and produce an instance that is
 * automatically synchronized between peers.
 *
 * Property changes (on any peer) generate a new instance (on all peers).
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoInstance] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * @param    onChange       Called after a new instance is generated, but before the
 *                          state of the fragment is updated.
 * @param    connect        A function to get the connection info. Typically, this is created by
 *                          a service call.
 *                          of this instance, validates that copy and then returns with it.
 * @param    binding        Set by the compiler plugin, ignore it.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass<A>> autoInstance(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    connect: suspend () -> AutoConnectInfo
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoInstance(binding, connect, onChange)

    binding.targetFragment.addProducer(store)

    return null
}