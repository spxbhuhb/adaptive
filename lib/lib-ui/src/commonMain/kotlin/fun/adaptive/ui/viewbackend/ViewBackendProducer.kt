package `fun`.adaptive.ui.viewbackend

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.general.Observable
import `fun`.adaptive.general.ObservableListener
import kotlin.reflect.KClass

class ViewBackendProducer<BACKEND_TYPE : Any>(
    override val binding: AdaptiveStateVariableBinding<BACKEND_TYPE>,
    val backend: Observable<BACKEND_TYPE>?
) : AdaptiveProducer<BACKEND_TYPE>, ObservableListener<BACKEND_TYPE> {

    override var latestValue: BACKEND_TYPE? = null

    override fun start() {
        backend?.addListener(this)
        latestValue = backend?.value
    }

    override fun stop() {
        backend?.removeListener(this)
    }

    override fun onChange(value: BACKEND_TYPE) {
        latestValue = value
        setDirtyBatch()
    }

    override fun toString(): String {
        return "ViewBackendProducer($binding)"
    }

}