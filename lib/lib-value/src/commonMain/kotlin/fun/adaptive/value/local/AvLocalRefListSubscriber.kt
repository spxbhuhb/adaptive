package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.value.AvRefListSpec
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId

/**
 * Subscribe a single [AvValue] and notify listeners when the value is updated from
 * the remote.
 */
open class AvLocalRefListSubscriber(
    conditions: List<AvSubscribeCondition>,
    backend: BackendAdapter,
    binding: AdaptiveStateVariableBinding<List<AvValueId>?>?
) : AvAbstractLocalSubscriber<List<AvValueId>?>(conditions, backend, binding) {

    override var value: List<AvValueId>? = null

    override fun process(value: AvValue<*>) {
        println("process: ${value}")
        val spec = value.spec

        if (spec !is AvRefListSpec) {
            getLogger("AvLocalRefListSubscriber").warning("invalid spec type: ${value.spec::class} != AvRefListSpec in ${value.adatToString()}")
            return
        }

        this.value = spec.refs
    }

}