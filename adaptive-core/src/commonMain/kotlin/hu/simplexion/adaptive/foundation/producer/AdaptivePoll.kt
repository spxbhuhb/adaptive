/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.producer

import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.service.transport.ServiceResultException
import hu.simplexion.adaptive.service.transport.ServiceTimeoutException
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
        CoroutineScope(binding.sourceFragment.adapter.dispatcher).also {
            scope = it
            it.launch {
                while (isActive) {
                    try {
                        binding.setValue(pollFunction.invokeSuspend(), false) // TODO check provider call in poll
                    } catch (e: AdaptiveProducerCancel) {
                        it.cancel()
                        break
                    } catch (e: ServiceTimeoutException) {
                        // TODO indicate the problem somehow
                        println(e)
                    } catch (e : ServiceResultException) {
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