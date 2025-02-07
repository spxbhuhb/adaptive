package `fun`.adaptive.foundation.value

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer

inline fun <VT> adaptiveValueStore(value: () -> VT): AdaptiveValueStore<VT> =
    AdaptiveValueStore(value())

@Producer
fun <VT> adaptiveValue(
    binding: AdaptiveStateVariableBinding<VT>? = null,
    producerFun: () -> AdaptiveValueStore<VT>
): VT? {
    checkNotNull(binding)

    binding.targetFragment.addProducer(
        AdaptiveValueProducer(binding, producerFun())
    )

    return null
}