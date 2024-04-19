/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.testing.TraceEvent
import kotlinx.coroutines.CoroutineDispatcher

interface AdaptiveAdapter<BT> : Adaptive {

    var rootFragment : AdaptiveFragment<BT>

    val rootBridge: AdaptiveBridge<BT>

    val dispatcher: CoroutineDispatcher

    val trace : Boolean

    val startedAt: Long

    fun createPlaceholder(): AdaptiveBridge<BT>

    fun newId(): Long

    fun trace(fragment: AdaptiveFragment<BT>, point: String, data : String) {
        TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data).println(startedAt)
    }

}