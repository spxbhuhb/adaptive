/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.producer

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding

interface AdaptiveProducer<T> {

    val binding: AdaptiveStateVariableBinding<T>

    var latestValue: T?

    fun start()

    fun stop()

    /**
     * Checks if this worker replaces [other]. If so [other] will be unmounted and disposed before
     * this worker is created and mounted.
     */
    fun replaces(other: AdaptiveProducer<*>) =
        (binding == other.binding && this::class == other::class)

    /**
     * Return with true if this producer has a value for the given state variable index.
     */
    fun hasValueFor(stateVariableIndex: Int): Boolean =
        binding.indexInTargetState == stateVariableIndex

    /**
     * Return with the latest produced value.
     */
    fun value(): Any? =
        latestValue

}