package `fun`.adaptive.value.remote

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscribeFun
import `fun`.adaptive.value.AvValue
import kotlin.reflect.KClass

/**
 * Subscribe a single [AvValue] and notify listeners when the value is updated from
 * the remote.
 */
open class AvRemoteValueSubscriber<SPEC : Any>(
    subscribeFun: AvSubscribeFun,
    backend: BackendAdapter,
    val specClass: KClass<SPEC>,
    binding: AdaptiveStateVariableBinding<AvValue<SPEC>?>? = null
) : AvAbstractRemoteSubscriber<AvValue<SPEC>?>(subscribeFun, backend, binding) {

    constructor(
        backend: BackendAdapter,
        specClass: KClass<SPEC>,
        condition: AvSubscribeCondition
    ) : this(
        { service, id -> listOf(condition).also { service.subscribe(it) } }, backend, specClass
    )

    constructor(
        binding: AdaptiveStateVariableBinding<AvValue<SPEC>?>,
        specClass: KClass<SPEC>,
        condition: AvSubscribeCondition
    ) : this(
        { service, id -> listOf(condition).also { service.subscribe(it) } },
        binding.targetFragment.adapter.backend,
        specClass,
        binding
    )

    override var value: AvValue<SPEC>? = null

    override fun process(value: AvValue<*>) {
        if (! specClass.isInstance(value.spec)) {
            getLogger("AvRemoteValueSubscriber").warning("invalid spec type: ${value.spec::class} != $specClass in ${value.adatToString()}")
            return
        }

        @Suppress("UNCHECKED_CAST")
        this.value = value as AvValue<SPEC>
    }

}