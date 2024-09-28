package `fun`.adaptive.adat.store

import `fun`.adaptive.adat.*
import `fun`.adaptive.adat.api.*
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
fun <A : AdatClass> copyStore(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    source: () -> A
): A {
    checkNotNull(binding)

    val store = CopyStore(binding, source(), onChange)

    binding.targetFragment.addProducer(store)

    return store.latestValue !!
}

class CopyStore<A : AdatClass>(
    override val binding: AdaptiveStateVariableBinding<A>,
    initialValue: A,
    val onChange: ((newValue: A) -> Unit)?
) : AdatStore<A>(), AdaptiveProducer<A> {

    val adatContext = AdatContext<Any>(null, null, null, store = this, null)

    override var latestValue: A? = makeCopy(initialValue, null, false)

    @Deprecated("use update instead")
    fun setProperty(path: List<String>, value: Any?) {
        update(latestValue !!, path.toTypedArray(), value)
    }

    override fun update(original: A, newValue: A) {
        makeCopy(newValue, null, true)
        setDirty()
    }

    override fun update(newValue: A) {
        makeCopy(newValue, null, true)
        setDirty()
    }

    override fun update(instance: A, path: Array<String>, value: Any?) {
        val current = requireNotNull(latestValue) { "missing latest value" }
        makeCopy(current, AdatChange(path.toList(), value), patch = true)
    }

    fun makeCopy(value: A, change: AdatChange?, patch: Boolean) =
        value.deepCopy(change).also {
            it.applyContext(adatContext)
            it.validateForContext()
            latestValue = it
            onChange?.invoke(it)
            if (patch) {
                setDirty()
            }
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