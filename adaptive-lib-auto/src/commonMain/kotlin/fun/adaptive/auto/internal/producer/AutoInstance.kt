package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding

class AutoInstance<A : AdatClass>(
    binding: AdaptiveStateVariableBinding<A>,
    connect: suspend () -> AutoConnectInfo,
    val onCommit: ((newValue: A) -> Unit)?,
    trace: Boolean
) : ProducerBase<PropertyBackend, AdatClassFrontend<A>, A>(
    binding, connect, trace
) {
    override var latestValue: A? = null

    val itemId
        get() = LamportTimestamp(0, 0) // does not matter for single instances

    @Suppress("UNCHECKED_CAST") // TODO should we create a binding for adat classes specifically?
    val companion = binding.adatCompanion !! as AdatCompanion<A>

    override fun build() {

        backend = PropertyBackend(
            context,
            itemId,
            companion.wireFormatName,
            initialValues = null
        )

        frontend = AdatClassFrontend(
            backend,
            companion.adatWireFormat,
            initialValue = null,
            itemId = null,
            collectionFrontend = null,
            onCommit = {
                latestValue = frontend.value
                onCommit?.invoke(latestValue!!)
                setDirty() // TODO make a separate binding for producers
            }
        )

    }

    override fun defaultMetadata(): AdatClassMetadata =
        companion.adatMetadata

    override fun defaultWireFormat(): AdatClassWireFormat<*> =
        companion.adatWireFormat

    override fun toString(): String {
        return "AutoInstance($binding)"
    }

}