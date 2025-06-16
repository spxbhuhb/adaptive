package `fun`.adaptive.value.remote

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlin.reflect.KClass

/**
 * [Producer](def://) for a list of [values](def://) from the [remote value store](def://).
 */
@Producer
fun <SPEC : Any> avRemoteList(
    marker: AvMarker,
    specClass: KClass<SPEC>,
    transform: ((Map<AvValueId, AvValue<SPEC>>) -> List<AvValue<SPEC>>)? = null,
    binding: AdaptiveStateVariableBinding<List<AvValue<SPEC>>?>? = null
): List<AvValue<SPEC>>? {

    checkNotNull(binding) { "binding is required for avRemoteList" }

    val subscriber = AvRemoteListSubscriber(
        binding,
        specClass,
        transform,
        AvSubscribeCondition(marker = marker)
    )

    binding.targetFragment.addProducer(subscriber)

    return null
}