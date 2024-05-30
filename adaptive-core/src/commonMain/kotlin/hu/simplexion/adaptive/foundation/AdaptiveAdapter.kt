/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.testing.TraceEvent
import kotlinx.coroutines.CoroutineDispatcher

interface AdaptiveAdapter {

    val fragmentFactory: AdaptiveFragmentFactory

    var rootFragment: AdaptiveFragment

    val rootContainer: Any

    val dispatcher: CoroutineDispatcher

    var trace: Array<out Regex>

    val startedAt: Long

    fun newId(): Long

    fun actualize(name: String, parent: AdaptiveFragment, index: Int) =
        fragmentFactory.newInstance(name, parent, index)

    fun addActual(fragment: AdaptiveFragment) = Unit

    fun removeActual(fragment: AdaptiveFragment) = Unit

    /**
     * Called by the `adaptive` entry point function after the root fragment is mounted.
     */
    fun mounted() = Unit

    fun trace(fragment: AdaptiveFragment, point: String, data: String) {
        if (fragment.trace && fragment.tracePatterns.any { it.matches(point) }) {
            TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data).println(startedAt)
        }
    }

    fun trace(point: String, data: String) {
        if (trace.any { it.matches(point) }) {
            TraceEvent("<adapter>", - 1, point, data).println(startedAt)
        }
    }

}