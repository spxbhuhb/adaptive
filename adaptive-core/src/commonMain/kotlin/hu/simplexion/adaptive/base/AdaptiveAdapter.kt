/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base

import hu.simplexion.adaptive.base.registry.AdaptiveFragmentImplRegistry
import hu.simplexion.adaptive.base.testing.TraceEvent
import kotlinx.coroutines.CoroutineDispatcher

interface AdaptiveAdapter<BT> {

    val fragmentImplRegistry : AdaptiveFragmentImplRegistry<BT>
        get() = throw UnsupportedOperationException("This adapter does not provide a fragment registry.")

    var rootFragment : AdaptiveFragment<BT>

    val rootBridge: AdaptiveBridge<BT>

    val dispatcher: CoroutineDispatcher

    val trace : Boolean

    val startedAt: Long

    fun actualize(name : String, parent : AdaptiveFragment<BT>, index: Int) =
        fragmentImplRegistry.actualize(name, parent, index)

    fun createPlaceholder(): AdaptiveBridge<BT>

    fun newId(): Long

    fun trace(fragment: AdaptiveFragment<BT>, point: String, data : String) {
        TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data).println(startedAt)
    }

    /**
     * Called by the `adaptive` entry point function after the root fragment is mounted.
     */
    fun mounted() {

    }

}