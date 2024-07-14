package hu.simplexion.adaptive.adat.store

import hu.simplexion.adaptive.adat.AdatChange
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatContext
import hu.simplexion.adaptive.adat.deepCopy
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
 * @param    source    Source of the stored data. [copyStore] makes a deep copy
 *                     of this instance, validates that copy and then returns with it.
 * @param    binding   Set by the compiler plugin, ignore it.
 *
 * @return   A validated copy of [source].
 */
@Producer
fun <A : AdatClass<A>> copyStore(
    binding: AdaptiveStateVariableBinding<A>? = null,
    source: () -> A
): A {
    checkNotNull(binding)

    val store = CopyStore(binding, source())

    binding.targetFragment.addProducer(store)

    return store.latestValue !!
}

class CopyStore<A : AdatClass<A>>(
    override val binding: AdaptiveStateVariableBinding<A>,
    initialValue: A
) : AdatStore(), AdaptiveProducer<A> {

    override var latestValue: A? = makeCopy(initialValue, null, null)


    fun setValue(path: List<String>, value: Any?) {
        latestValue = latestValue?.let { makeCopy(it, AdatChange(path, value), it.adatContext) }
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    fun makeCopy(value: A, change: AdatChange?, context: AdatContext?) =
        value.deepCopy(change).also {
            it.adatContext = context ?: AdatContext(store = this)
            it.validate()
        }

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