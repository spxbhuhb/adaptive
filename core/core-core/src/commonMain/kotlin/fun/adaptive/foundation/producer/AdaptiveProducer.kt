/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.producer

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import kotlin.js.JsName

interface AdaptiveProducer<T> {

    // nullable to support classes which are producers and independent stores
    // at the same time, like AdaptiveCopyStore

    val binding: AdaptiveStateVariableBinding<T>?

    var latestValue: T?

    /**
     * When true, this producer will be calle
     */
    val actual : Boolean
        get() = false

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
        binding?.indexInTargetState == stateVariableIndex

    /**
     * Return with the latest produced value.
     */
    @JsName("valueFun") // TODO clean up the confusion between `value()` and store `value`
    fun value(): Any? =
        latestValue

    fun setDirtyBatch() {
        binding?.apply { targetFragment.setDirtyBatch(indexInTargetState) }
    }

    /**
     * Called from [AdaptiveFragment.addActual] when a [actual] is true.
     */
    fun addActual(fragment: AdaptiveFragment) {

    }

    /**
     * Called from [AdaptiveFragment.removeActual] when a [actual] is true.
     */
    fun removeActual(fragment: AdaptiveFragment) {

    }
}