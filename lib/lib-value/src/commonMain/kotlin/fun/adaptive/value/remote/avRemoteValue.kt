package `fun`.adaptive.value.remote

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlin.reflect.KClass

/**
 * [Producer](def://) for a [value](def://) from the [remote value store](def://).
 */
@Producer
fun <SPEC : Any> avRemoteValue(
    valueId: AvValueId?,
    specClass: KClass<SPEC>,
    binding: AdaptiveStateVariableBinding<AvValue<SPEC>?>? = null
): AvValue<SPEC>? {

    checkNotNull(binding) { "binding is required for avRemoteValue" }

    val subscriber = AvRemoteValueSubscriber(
        binding,
        specClass,
        AvSubscribeCondition(valueId)
    )

    binding.targetFragment.addProducer(subscriber)

    return null
}