package `fun`.adaptive.value.local

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlin.reflect.KClass

/**
 * [Producer](def://) for a [value](def://) from the [local value store](def://).
 */
@Producer
fun <SPEC : Any> avLocalValue(
    valueId: AvValueId?,
    specClass: KClass<SPEC>,
    binding: AdaptiveStateVariableBinding<AvValue<SPEC>?>? = null
): AvValue<SPEC>? {

    checkNotNull(binding) { "binding is required for avLocalValue" }

    val subscriber =  AvLocalValueSubscriber(
        listOf(AvSubscribeCondition(valueId)),
        binding.targetFragment.adapter.backend,
        specClass,
        binding
    )

    binding.targetFragment.addProducer(subscriber)

    return null
}