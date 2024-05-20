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

    val trace: Boolean

    val startedAt: Long

    fun newId(): Long

    fun actualize(name: String, parent: AdaptiveFragment, index: Int) =
        fragmentFactory.newInstance(name, parent, index)

    fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) = Unit
    fun removeActual(fragment: AdaptiveFragment) = Unit
    fun addAnchor(fragment: AdaptiveFragment, higherAnchor: AdaptiveFragment?) = Unit
    fun removeAnchor(fragment: AdaptiveFragment) = Unit

    /**
     * Called by the `adaptive` entry point function after the root fragment is mounted.
     */
    fun mounted() = Unit

    fun trace(fragment: AdaptiveFragment, point: String, data: String) {
        TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data).println(startedAt)
    }

}