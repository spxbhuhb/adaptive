/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.producer

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.service.transport.ServiceCallException
import hu.simplexion.adaptive.service.transport.ServiceTimeoutException
import kotlinx.coroutines.*
import kotlin.time.Duration

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

    binding.targetFragment.addProducer(
        AdaptivePoll(binding, interval, producerFun)
    )

    return null
}

class AdaptivePoll<VT>(
    override val binding: AdaptiveStateVariableBinding<VT>,
    val interval: Duration,
    val pollFunction: suspend () -> VT
) : AdaptiveProducer<VT> {

    var scope: CoroutineScope? = null

    override var latestValue: VT? = null

    override fun start() {
        CoroutineScope(binding.targetFragment.adapter.dispatcher).also {
            scope = it
            it.launch {
                while (isActive) {
                    // TODO do not poll when the fragment is not mounted

                    try {
                        latestValue = pollFunction()
                        binding.targetFragment.setDirty(binding.indexInTargetState, true) // TODO make a separate binding for producers
                    } catch (e: AdaptiveProducerCancel) {
                        it.cancel()
                        break
                    } catch (e: ServiceTimeoutException) {
                        // TODO indicate the problem somehow
                        println(e)
                    } catch (e: ServiceCallException) {
                        // TODO indicate the problem somehow
                        println(e)
                    }

                    // TODO this delays after errors as well, maybe consider a bit more sophisticated scheduling
                    delay(interval)
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