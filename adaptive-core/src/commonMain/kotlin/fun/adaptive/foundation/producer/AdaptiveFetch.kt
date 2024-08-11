/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.producer

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.service.transport.ServiceCallException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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

    binding.targetFragment.addProducer(
        AdaptiveFetch(binding, producerFun)
    )

    return null
}

class AdaptiveFetch<VT>(
    override val binding: AdaptiveStateVariableBinding<VT>,
    val fetchFunction: suspend () -> VT
) : AdaptiveProducer<VT> {

    var scope: CoroutineScope? = null

    override var latestValue: VT? = null

    override fun start() {
        CoroutineScope(binding.targetFragment.adapter.dispatcher).also {
            scope = it
            it.launch {
                try {
                    latestValue = fetchFunction()
                    binding.targetFragment.setDirty(binding.indexInTargetState, true)
                } catch (e: AdaptiveProducerCancel) {
                    it.cancel()
                } catch (e: TimeoutCancellationException) {
                    // TODO indicate the problem somehow
                    println(e)
                } catch (e: ServiceCallException) {
                    // TODO indicate the problem somehow
                    println(e)
                }
            }
        }
    }

    override fun stop() {
        checkNotNull(scope).cancel()
        scope = null
    }

    override fun toString(): String {
        return "AdaptiveFetch($binding)"
    }

}