package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlin.reflect.KClass

/**
 * Subscribe a single [AvValue] and notify listeners when the value is updated from
 * the remote.
 */
open class AvLocalValueSubscriber<SPEC : Any>(
    conditions: List<AvSubscribeCondition>,
    backend: BackendAdapter,
    val specClass: KClass<SPEC>,
    binding: AdaptiveStateVariableBinding<AvValue<SPEC>?>?
) : AvAbstractLocalSubscriber<AvValue<SPEC>?>(conditions, backend, binding) {

    override var value: AvValue<SPEC>? = null

    override fun process(value: AvValue<*>) {
        if (! specClass.isInstance(value.spec)) {
            getLogger("AvLocalValueSubscriber").warning("invalid spec type: ${value.spec::class} != $specClass in ${value.adatToString()}")
            return
        }

        @Suppress("UNCHECKED_CAST")
        this.value = value as AvValue<SPEC>
    }

    override fun processRemove(valueId: AvValueId) {
        this.value = null // the value no longer exists
    }

}