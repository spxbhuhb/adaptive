package `fun`.adaptive.adat.testing

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanionResolve
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.producer.Producer

/**
 * A producer to be used for testing.
 */
@Producer
@Suppress("unused") // used by plugin test code
fun <A : AdatClass> testStore(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    source: () -> A
): A {
    checkNotNull(binding)

    val store = TestStore(binding, source(), onChange)

    binding.targetFragment.addProducer(store)

    return store.latestValue !!
}

/**
 * A nullable producer to be used for testing. It is similar to `copyStore`.
 */
@Producer
@Suppress("unused") // used by plugin test code
fun <A : AdatClass> testStoreOrNull(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    source: () -> A?
): A? {
    checkNotNull(binding)

    val store = TestStore(binding, source(), onChange)

    binding.targetFragment.addProducer(store)

    return store.latestValue
}

/**
 * A producer to be used for testing with companion resolution.
 */
@Producer
@AdatCompanionResolve
@Suppress("unused") // used by plugin test code
fun <A : AdatClass> testStoreOrNullWithCompanion(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    source: () -> A?
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = TestStore(binding, source(), onChange)

    binding.targetFragment.addProducer(store)

    return store.latestValue
}

class TestStore<A : AdatClass>(
    override val binding: AdaptiveStateVariableBinding<A>,
    initialValue: A?,
    val onChange: ((newValue: A) -> Unit)?
) : AdatStore<A>(), AdaptiveProducer<A> {

    val adatContext = AdatContext<Any>(null, null, null, store = this, null)

    override var value: A
        get() = checkNotNull(latestValue) { "missing copyStore value" }
        set(value) {
            update(value)
        }

    override var latestValue: A? = initialValue?.let { makeCopy(it, null, false) }

    override fun update(newValue: A) {
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

    fun makeCopy(value: A, change: AdatChange?, patch: Boolean) =
        value.deepCopy(change).also {
            adatContext.apply(it)
            it.validateForContext()
            latestValue = it
            if (patch) {
                onChange?.invoke(it)
                setDirtyBatch()
            }
        }

    override fun start() {
        // copy store is event-driven
    }

    override fun stop() {
        // copy store is event-driven
    }

    override fun toString(): String {
        return "TestStore($binding)"
    }

}