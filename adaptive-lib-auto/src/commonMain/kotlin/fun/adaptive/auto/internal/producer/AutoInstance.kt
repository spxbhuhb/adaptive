package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.applyContext
import `fun`.adaptive.adat.deepCopy
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding

class AutoInstance<A : AdatClass<A>>(
    binding: AdaptiveStateVariableBinding<A>,
    connect: suspend () -> AutoConnectInfo,
    val onCommit: ((newValue: A) -> Unit)?,
    trace: Boolean
) : AutoProducerBase<A>(binding, connect, trace) {

    override var latestValue: A? = null

    @Suppress("UNCHECKED_CAST") // TODO should we create a binding for adat classes specifically?
    val companion = binding.adatCompanion !! as AdatCompanion<A>

    override val backend: PropertyBackend
        get() = propertyBackend

    lateinit var propertyBackend: PropertyBackend
        private set

    override val frontend: AdatClassFrontend<A>
        get() = adatClassFrontend

    lateinit var adatClassFrontend: AdatClassFrontend<A>
        private set

    val itemId
        get() = LamportTimestamp(0, 0) // does not matter for single instances

    fun replaceValue(newValue: A) {
        latestValue = makeCopy(newValue, null)
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    fun setProperty(path: List<String>, value: Any?) {
        latestValue = latestValue?.let { makeCopy(it, AdatChange(path, value)) }
        onCommit?.invoke(latestValue !!)
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    fun makeCopy(value: A, change: AdatChange?) =
        value.deepCopy(change).also { it.applyContext(AdatContext(null, null, null, this, null)) }

    override fun createBackendAndFrontend() {
        propertyBackend = PropertyBackend(
            backendContext,
            itemId,
            metadataId = null,
            initialValues = null
        )

        adatClassFrontend = AdatClassFrontend(
            propertyBackend,
            companion,
            initialValue = null,
            itemId = null,
            collectionFrontend = null,
            onCommit = {
                latestValue = adatClassFrontend.value
                binding.targetFragment.setDirty(binding.indexInTargetState, true) // TODO make a separate binding for producers
            }
        )
    }

    override fun defaultMetadata(): AdatClassMetadata<*> =
        companion.adatMetadata

    override fun defaultWireFormat(): AdatClassWireFormat<*> =
        companion.adatWireFormat

    override fun toString(): String {
        return "AutoInstance($binding)"
    }

}