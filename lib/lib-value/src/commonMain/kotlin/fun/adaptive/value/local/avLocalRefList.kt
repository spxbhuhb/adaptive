package `fun`.adaptive.value.local

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.value.AvRefLabel
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId

/**
 * [Producer](def://) for a list of [value references](def://).
 *
 * @param  [valueId]  Id of the [value reference list](def://).
 */
@Producer
fun avLocalRefList(
    valueId: AvValueId?,
    binding: AdaptiveStateVariableBinding<List<AvValueId>?>? = null
): List<AvValueId>? {

    checkNotNull(binding) { "binding is required for avLocalRefList" }

    val subscriber = AvLocalRefListSubscriber(
        listOf(AvSubscribeCondition(valueId)),
        binding.targetFragment.adapter.backend,
        binding
    )

    binding.targetFragment.addProducer(subscriber)

    return null
}

/**
 * [Producer](def://) for a list of [value references](def://).
 *
 * @param  [value]  Id of the [referring value](def://).
 * @param  [refLabel]  The reference label of the [value reference list](def://).
 */
@Producer
fun avLocalRefList(
    value: AvValue<*>,
    refLabel: AvRefLabel,
    binding: AdaptiveStateVariableBinding<List<AvValueId>?>? = null
): List<AvValueId>? {
    return avLocalRefList(value.refs[refLabel], binding)
}