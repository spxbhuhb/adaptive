package `fun`.adaptive.foundation.value

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdaptiveCopyStore
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.general.Observable

inline fun <VT> storeFor(value : () -> VT) : Observable<VT> = adaptiveStoreFor(value())

fun <VT> adaptiveStoreFor(value : VT): Observable<VT> =
    if (value is AdatClass) {
        AdaptiveCopyStore(null, value)
    } else {
        AdaptiveValueStore(value)
    }

@Producer
fun <VT> valueFrom(
    binding: AdaptiveStateVariableBinding<VT>? = null,
    producerFun: () -> Observable<VT>
): VT {
    checkNotNull(binding)

    val store = producerFun()

    binding.targetFragment.addProducer(
        AdaptiveValueProducer(binding, store)
    )

    return store.value
}

// FIXME does valueFromOrNull properly refreshes the producer on producer fun change?
@Producer
fun <VT> valueFromOrNull(
    binding: AdaptiveStateVariableBinding<VT>? = null,
    producerFun: () -> Observable<VT>?
): VT? {
    checkNotNull(binding)

    val store = producerFun()

    binding.targetFragment.addProducer(
        AdaptiveValueProducer(binding, store)
    )

    return store?.value
}