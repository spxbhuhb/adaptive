/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.producer

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.service.transport.ServiceResultException
import hu.simplexion.adaptive.service.transport.ServiceTimeoutException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AdaptiveFetch<VT>(
    val binding: AdaptiveStateVariableBinding<VT>,
    val fetchFunction: suspend () -> VT
) : AdaptiveProducer {

    var scope: CoroutineScope? = null

    var latestValue : VT? = null

    override fun replaces(other: AdaptiveProducer): Boolean =
        other is AdaptiveFetch<*> && other.binding == this.binding

    override fun start() {
        CoroutineScope(binding.sourceFragment.adapter.dispatcher).also {
            scope = it
            it.launch {
                try {
                    latestValue = fetchFunction()
                    binding.targetFragment.setDirty(binding.indexInTargetState, true)
                } catch (e: AdaptiveProducerCancel) {
                    it.cancel()
                } catch (e: ServiceTimeoutException) {
                    // TODO indicate the problem somehow
                    println(e)
                } catch (e: ServiceResultException) {
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

    override fun hasValueFor(stateVariableIndex : Int) =
        binding.indexInTargetState == stateVariableIndex

    override fun value() =
        latestValue

    override fun toString(): String {
        return "AdaptiveFetch($binding)"
    }

}