/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("UNUSED_PARAMETER")

package hu.simplexion.adaptive.foundation.producer

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import kotlin.time.Duration

/**
 * Execute [pollFun] once in every [interval]. Adds an [AdaptivePoll] producer to
 * the given component.
 *
 * @param  interval  The interval of execution.
 * @param  pollFun  The poll function to call.
 */
fun <VT> poll(
    interval: Duration,
    default: VT,
    binding: AdaptiveStateVariableBinding<VT>? = null,
    pollFun: suspend () -> VT
): VT {
    checkNotNull(binding)

    binding.sourceFragment.addProducer(
        AdaptivePoll(binding, interval, pollFun)
    )

    return default
}

/**
 * Cancels the producer by returning from the coroutine. Throws [AdaptiveProducerCancel].
 */
fun cancelProducer(message : String? = null) : Nothing {
    throw AdaptiveProducerCancel(message)
}