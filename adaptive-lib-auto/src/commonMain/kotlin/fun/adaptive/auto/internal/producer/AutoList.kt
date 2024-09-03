package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding

class AutoList<A : AdatClass<A>>(
    binding: AdaptiveStateVariableBinding<List<A>>,
    connect: suspend () -> AutoConnectInfo,
    val companion: AdatCompanion<A>,
    val onListCommit: ((newValue: List<A>) -> Unit)?,
    val onItemCommit: ((item: A) -> Unit)?,
    trace: Boolean
) : AutoProducerBase<List<A>>(binding, connect, trace) {

    override var latestValue: List<A>? = null

    override val backend: SetBackend
        get() = setBackend

    lateinit var setBackend: SetBackend
        private set

    override val frontend: AdatClassListFrontend<A>
        get() = listFrontend

    lateinit var listFrontend: AdatClassListFrontend<A>
        private set

    override fun createBackendAndFrontend() {
        setBackend = SetBackend(backendContext)

        listFrontend = AdatClassListFrontend(
            setBackend,
            companion,
            onListCommit = {
                latestValue = it
                onListCommit?.invoke(it)
                binding.targetFragment.setDirty(binding.indexInTargetState, true) // TODO make a separate binding for producers
            },
            onItemCommit = { list, instance ->
                latestValue = list
                onItemCommit?.invoke(instance)
                binding.targetFragment.setDirty(binding.indexInTargetState, true) // TODO make a separate binding for producers
            }
        )
    }

    override fun defaultMetadata(): AdatClassMetadata =
        companion.adatMetadata

    override fun defaultWireFormat(): AdatClassWireFormat<*> =
        companion.adatWireFormat

    override fun toString(): String {
        return "AutoSet($binding)"
    }

}