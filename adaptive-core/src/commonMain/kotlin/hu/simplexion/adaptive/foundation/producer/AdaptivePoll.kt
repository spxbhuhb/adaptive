/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.producer

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.service.transport.ServiceResultException
import hu.simplexion.adaptive.service.transport.ServiceTimeoutException
import kotlinx.coroutines.*
import kotlin.time.Duration

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
                    } catch (e: ServiceResultException) {
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