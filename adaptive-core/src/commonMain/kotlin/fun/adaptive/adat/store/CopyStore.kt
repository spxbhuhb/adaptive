package `fun`.adaptive.adat.store

import `fun`.adaptive.adat.*
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.producer.Producer

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

    fun replaceValue(newValue: A) {
        latestValue = makeCopy(newValue, null)
        latestValue?.validate()
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    @Deprecated("use update instead")
    fun setProperty(path: List<String>, value: Any?) {
        update(latestValue!!, path.toTypedArray(), value)
    }

    override fun update(instance: AdatClass<*>, path: Array<String>, value: Any?) {
        val current = requireNotNull(latestValue)
        val new = makeCopy(current, AdatChange(path.toList(), value))
        new.validate()
        latestValue = new
        onChange?.invoke(new)
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    fun makeCopy(value: A, change: AdatChange?) =
        value.deepCopy(change).also { it.applyContext(AdatContext<Any>(null, null, null, this, null)) }

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

fun <A : AdatClass<A>> A.replaceWith(newValue: A) {
    val store = requireNotNull(adatContext?.store) { "cannot replace the adat class without a store: ${getMetadata().name}" }
    require(store is CopyStore<*>) { "store is not a copy store" }

    @Suppress("UNCHECKED_CAST") // replaceWith enforces the same type
    (store as CopyStore<A>).replaceValue(newValue)
}