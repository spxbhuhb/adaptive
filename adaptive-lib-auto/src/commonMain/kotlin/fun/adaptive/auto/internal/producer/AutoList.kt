package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding

class AutoList<A : AdatClass>(
    binding: AdaptiveStateVariableBinding<List<A>>,
    connect: suspend () -> AutoConnectionInfo<List<A>>?,
    override val defaultWireFormat: AdatClassWireFormat<*>? = null,
    val listener: AutoCollectionListener<A>? = null,
    peer: AutoInstance<*, *, List<A>, A>? = null,
    trace: Boolean,
) : ProducerBase<SetBackend<A>, AdatClassListFrontend<A>, List<A>, A>(binding, connect, peer, trace) {

    override var latestValue: List<A>? = null

    override fun build() {

        context.addListener(ProducerListener())
        if (listener != null) context.addListener(listener)

        backend = SetBackend(context)

        frontend = AdatClassListFrontend<A>(backend)
    }

    override fun toString(): String {
        return "AutoList($binding)"
    }

    private inner class ProducerListener: AutoCollectionListener<A>() {

        override fun onChange(newValue: List<A>) {
            if (! syncEnd) return
            latestValue = frontend.values
            setDirty() // TODO make a separate binding for producers
        }

        override fun onChange(newValue: A, oldValue : A?) {
            if (! syncEnd) return
            latestValue = frontend.values
            setDirty() // TODO make a separate binding for producers
        }

        override fun onSyncEnd() {
            syncEnd = true
            latestValue = frontend.values
            setDirty() // TODO make a separate binding for producers
        }
    }

}