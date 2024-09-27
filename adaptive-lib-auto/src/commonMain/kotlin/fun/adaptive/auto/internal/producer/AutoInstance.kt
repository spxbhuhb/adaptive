package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoListener
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding

class AutoInstance<A : AdatClass>(
    binding: AdaptiveStateVariableBinding<A>,
    connect: suspend () -> AutoConnectionInfo<A>,
    val listener: AutoListener<A>? = null,
    peer: OriginBase<*, *, A, A>? = null,
    trace: Boolean
) : ProducerBase<PropertyBackend<A>, AdatClassFrontend<A>, A, A>(
    binding, connect, peer, trace
) {
    override var latestValue: A? = null

    val itemId
        get() = LamportTimestamp.CONNECTING // does not matter for single instances

    @Suppress("UNCHECKED_CAST") // TODO should we create a binding for adat classes specifically?
    val companion = binding.adatCompanion !! as AdatCompanion<A>

    override val defaultWireFormat: AdatClassWireFormat<*>?
        get() = companion.adatWireFormat

    override fun build() {

        context.addListener(ProducerListener())
        if (listener != null) context.addListener(listener)

        val values = arrayOfNulls<Any?>(companion.adatMetadata.properties.size)

        backend = PropertyBackend<A>(
            context,
            itemId,
            companion.wireFormatName,
            values
        )

        frontend = AdatClassFrontend(
            backend,
            companion.adatWireFormat,
            initialValue = null,
            itemId = itemId,
            collectionFrontend = null
        )

    }

    override fun toString(): String {
        return "AutoInstance($binding)"
    }


    private inner class ProducerListener : AutoListener<A>() {
        override fun onItemCommit(item: A) {
            item.validateForContext()
            latestValue = item
            setDirty() // TODO make a separate binding for producers
        }
    }

}