package hu.simplexion.adaptive.adat.store

import hu.simplexion.adaptive.adat.*
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.producer.AdaptiveProducer
import hu.simplexion.adaptive.foundation.producer.Producer

/**
 * A producer that generates a new copy of the stored instance whenever
 * the `setValue` function is called.
 *
 * Each new copy is validated by default, so fragments that use values
 * produced by [copyStore] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * @param    onChange  Called after a new instance is generated, but before the
 *                     state of the fragment is updated.
 * @param    source    Source of the stored data. [copyStore] makes a deep copy
 *                     of this instance, validates that copy and then returns with it.
 * @param    binding   Set by the compiler plugin, ignore it.
 *
 * @return   A validated copy of [source].
 */
@Producer
fun <A : AdatClass<A>> copyStore(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    source: () -> A
): A {
    checkNotNull(binding)

    val store = CopyStore(binding, source(), onChange)

    binding.targetFragment.addProducer(store)

    return store.latestValue !!
}

class CopyStore<A : AdatClass<A>>(
    override val binding: AdaptiveStateVariableBinding<A>,
    initialValue: A,
    val onChange: ((newValue: A) -> Unit)?
) : AdatStore(), AdaptiveProducer<A> {

    override var latestValue: A? = makeCopy(initialValue, null)

    fun setProperty(path: List<String>, value: Any?) {
        latestValue = latestValue?.let { makeCopy(it, AdatChange(path, value)) }
        onChange?.invoke(latestValue !!)
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    fun makeCopy(value: A, change: AdatChange?) =
        value.deepCopy(change).also { it.applyContext(AdatContext(null, null, this, null)) }

    override fun start() {
        // copy store is event-driven
    }

    override fun stop() {
        // copy store is event-driven
    }

    override fun toString(): String {
        return "CopyStore($binding)"
    }

}