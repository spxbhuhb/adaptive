/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.base.producer

import hu.simplexion.adaptive.base.internal.BoundSupportFunction
import hu.simplexion.adaptive.base.binding.AdaptiveStateVariableBinding
import kotlinx.coroutines.*
import kotlin.time.Duration

class AdaptivePoll<VT>(
    val binding: AdaptiveStateVariableBinding<VT>,
    val interval: Duration
) : AdaptiveProducer {

    val pollFunction = BoundSupportFunction(
        binding.sourceFragment,
        binding.sourceFragment,
        binding.supportFunctionIndex
    )

    var scope: CoroutineScope? = null

    override fun replaces(other: AdaptiveProducer): Boolean =
        other is AdaptivePoll<*> && other.binding == this.binding

    override fun start() {
        scope = CoroutineScope(binding.sourceFragment.adapter.dispatcher).apply {
            launch {
                while (isActive) {

                    try {
                        binding.setValue(pollFunction.invokeSuspend(), false) // TODO check provider call in poll
                        delay(interval)

                    } catch (e: AdaptiveProducerCancel) {
                        cancel()
                        break
                    }
                }
            }
        }
    }

    override fun stop() {
        checkNotNull(scope).cancel()
        scope = null
    }

    override fun toString(): String {
        return "AdaptivePoll($binding, $interval)"
    }

}