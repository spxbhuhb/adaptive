package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding

class AutoListPoly<A : AdatClass>(
    binding: AdaptiveStateVariableBinding<List<A>>,
    connect: suspend () -> AutoConnectInfo<List<A>>,
    override val defaultWireFormat: AdatClassWireFormat<*>? = null,
    val onListCommit: ((newValue: List<A>) -> Unit)?,
    val onItemCommit: ((item: A) -> Unit)?,
    trace: Boolean
) : ProducerBase<SetBackend, AdatClassListFrontend<*>, List<A>>(binding, connect, trace) {

    override var latestValue: List<A>? = null

    override fun build() {
        // TODO merge AutoList and AutoListPoly
        backend = SetBackend(context)

        frontend = AdatClassListFrontend(
            backend,
            onListCommit = {
                latestValue = it
                onListCommit?.invoke(it)
                setDirty() // TODO make a separate binding for producers
            },
            onItemCommit = { list, instance ->
                latestValue = list
                onItemCommit?.invoke(instance)
                setDirty() // TODO make a separate binding for producers
            }
        )
    }

    override fun toString(): String {
        return "AutoPolyList($binding)"
    }

}