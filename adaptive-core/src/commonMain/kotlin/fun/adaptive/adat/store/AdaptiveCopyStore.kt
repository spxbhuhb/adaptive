package `fun`.adaptive.adat.store

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.general.ObservableListener

/**
 * A producer that generates a new copy of the [source] whenever the `update`
 * function is called.
 *
 * Each new copy is validated by default, so fragments that use values
 * produced by [copyOf] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * @param    onChange  Called after a new instance is generated, but before the
 *                     state of the fragment is updated.
 * @param    source    Source of the stored data. [copyOf] makes a deep copy
 *                     of this instance, validates that copy and then returns with it.
 * @param    binding   Set by the compiler plugin, ignore it.
 *
 * @return   A validated copy of [source].
 */
@Producer
fun <A : AdatClass> copyOf(
    onChange: ObservableListener<A>? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    source: () -> A
): A {
    checkNotNull(binding)

    val store = AdaptiveCopyStore(binding, source())

    if (onChange != null) {
        store.addListener(onChange)
    }

    binding.targetFragment.addProducer(store)

    return store.latestValue !!
}

class AdaptiveCopyStore<A>(
    override val binding: AdaptiveStateVariableBinding<A>?,
    initialValue: A
) : AdatStore<A>(), AdaptiveProducer<A> {

    val adatContext = AdatContext<Any>(null, null, null, store = this, null)

    override var value: A
        get() = checkNotNull(latestValue) { "missing copyStore value" }
        set(value) {
            update(value)
        }

    override var latestValue: A? = makeCopy(initialValue, null, false)

    override fun update(newValue: A) {
        makeCopy(newValue, null, true)
    }

    override fun update(original: A, newValue: A) {
        makeCopy(newValue, null, true)
    }

    override fun update(instance: A, path: Array<String>, value: Any?) {
        val current = requireNotNull(latestValue) { "missing copyStore value" }
        makeCopy(current, AdatChange(path.toList(), value), patch = true)
    }

    override fun setProblem(path: Array<String>, value: Boolean) {
        if (value) {
            adatContext.addProblem(path)
        } else {
            adatContext.clearProblem(path)
        }

        makeCopy(latestValue !!, null, patch = true)
    }

    @Suppress("UNCHECKED_CAST")
    fun makeCopy(value: A, change: AdatChange?, patch: Boolean) : A =
        (value as AdatClass).deepCopy(change).also {
            adatContext.apply(it)
            it.validateForContext()
            latestValue = it as A
            if (patch) {
                notifyListeners()
                setDirtyBatch()
            }
        } as A

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