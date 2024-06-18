/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.producer

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Execute [producerFun]. When the result is ready, set the state variable
 * the fetch is bound to.
 *
 * Use patterns:
 *
 * ```kotlin
 * val a = fetch { getA() }
 * val b = fetch { getB() } ?: B() // default value
 * ```
 *
 * @param    binding      Set by the compiler plugin, ignore it.
 * @param    producerFun  The function that produces the value.
 */
@Producer
fun <VT> fetch(
    binding: AdaptiveStateVariableBinding<VT>? = null,
    producerFun: suspend () -> VT
): VT? {
    checkNotNull(binding)

    binding.sourceFragment.addProducer(
        AdaptiveFetch(binding, producerFun)
    )

    return null
}

/**
 * Execute [producerFun] once in every [interval]. Adds an [AdaptivePoll] producer to
 * the given component.
 *
 * For non-suspend, non-null version use [periodic].
 *
 * Use patterns:
 *
 * ```kotlin
 * val a = poll(10.seconds) { getA() }
 * val b = poll(10.seconds) { getB() } ?: B() // default value
 * ```
 *
 * @param    interval     The interval of execution.
 * @param    binding      Set by the compiler plugin, ignore it.
 * @param    producerFun  The function that produces the value.
 */
@Producer
fun <VT> poll(
    interval: Duration,
    binding: AdaptiveStateVariableBinding<VT>? = null,
    producerFun: suspend () -> VT
): VT? {
    checkNotNull(binding)

    binding.sourceFragment.addProducer(
        AdaptivePoll(binding, interval, producerFun)
    )

    return null
}

/**
 * Execute [producerFun] once in every [interval]. Adds an [AdaptivePeriodic] producer to
 * the given component.
 *
 * For suspend version use [poll].
 *
 * Use patterns:
 *
 * ```kotlin
 * val a = periodic(10.seconds) { getA() }
 * ```
 *
 * @param    interval     The interval of execution.
 * @param    binding      Set by the compiler plugin, ignore it.
 * @param    producerFun  The function that produces the value.
 */
@Producer
fun <VT> periodic(
    interval: Duration,
    binding: AdaptiveStateVariableBinding<VT>? = null,
    producerFun: () -> VT
): VT {
    checkNotNull(binding)

    binding.sourceFragment.addProducer(
        AdaptivePoll(binding, interval, producerFun)
    )

    return producerFun()
}

/**
 * Cancels the producer by returning from the coroutine. Throws [AdaptiveProducerCancel].
 */
fun cancelProducer(message : String? = null) : Nothing {
    throw AdaptiveProducerCancel(message)
}