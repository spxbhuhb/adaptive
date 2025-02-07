package `fun`.adaptive.foundation.value

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer

class AdaptiveValueProducer<VT>(
    override val binding: AdaptiveStateVariableBinding<VT>,
    val store: AdaptiveValueStore<VT>
) : AdaptiveProducer<VT>, AdaptiveValueListener<VT> {

    override var latestValue: VT? = null

    override fun start() {
        store.addListener(this)
        latestValue = store.value
    }

    override fun stop() {
        store.removeListener(this)
    }

    override fun onValueChanged(value: VT) {
        latestValue = value
        setDirtyBatch()
    }

    override fun toString(): String {
        return "AdaptiveValueProducer($binding)"
    }

}