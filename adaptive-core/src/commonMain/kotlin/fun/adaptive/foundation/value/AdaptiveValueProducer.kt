package `fun`.adaptive.foundation.value

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.general.Observable
import `fun`.adaptive.general.ObservableListener

class AdaptiveValueProducer<VT>(
    override val binding: AdaptiveStateVariableBinding<VT>,
    val store: Observable<VT>?
) : AdaptiveProducer<VT>, ObservableListener<VT> {

    override var latestValue: VT? = null

    override fun start() {
        store?.addListener(this)
        latestValue = store?.value
    }

    override fun stop() {
        store?.removeListener(this)
    }

    override fun onChange(value: VT) {
        latestValue = value
        setDirtyBatch()
    }

    override fun toString(): String {
        return "AdaptiveValueProducer($binding)"
    }

}