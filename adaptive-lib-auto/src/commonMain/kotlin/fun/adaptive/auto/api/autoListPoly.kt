package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.producer.AutoListPoly
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer

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
fun autoListPoly(
    companion: AdatCompanion<*>,
    onListCommit: ((newValue: List<AdatClass<*>>) -> Unit)? = null,
    onItemCommit: ((item: AdatClass<*>) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<List<AdatClass<*>>>? = null,
    trace: Boolean = false,
    connect: suspend () -> AutoConnectInfo
): List<AdatClass<*>>? {
    checkNotNull(binding)

    val store = AutoListPoly(binding, connect, companion, onListCommit, onItemCommit, trace)

    binding.targetFragment.addProducer(store)

    return null
}