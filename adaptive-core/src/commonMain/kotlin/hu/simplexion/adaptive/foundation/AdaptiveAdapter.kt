/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.testing.TraceEvent
import kotlinx.coroutines.CoroutineDispatcher

interface AdaptiveAdapter {

    val fragmentFactory : AdaptiveFragmentFactory

    var rootFragment : AdaptiveFragment

    val rootContainer: Any

    val dispatcher: CoroutineDispatcher

    val trace : Boolean

    val startedAt: Long

    fun actualize(name : String, parent : AdaptiveFragment, index: Int) =
        fragmentFactory.newInstance(name, parent, index)

    fun createPlaceholder(parent : AdaptiveFragment, index : Int): AdaptiveFragment

    fun newId(): Long

    fun trace(fragment: AdaptiveFragment, point: String, data : String) {
        TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data).println(startedAt)
    }

    /**
     * Called by the `adaptive` entry point function after the root fragment is mounted.
     */
    fun mounted() {

    }

}