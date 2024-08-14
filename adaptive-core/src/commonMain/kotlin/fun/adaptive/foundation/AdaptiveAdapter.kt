/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation

import `fun`.adaptive.foundation.fragment.AdaptiveLoop
import `fun`.adaptive.foundation.fragment.AdaptiveSelect
import `fun`.adaptive.foundation.fragment.AdaptiveSequence
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.foundation.testing.TraceEvent
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlinx.coroutines.CoroutineDispatcher

interface AdaptiveAdapter {

    val fragmentFactory: AdaptiveFragmentFactory

    var rootFragment: AdaptiveFragment

    val rootContainer: Any

    val dispatcher: CoroutineDispatcher

    val backend : BackendAdapter
        get() = unsupported()

    var trace: Array<out Regex>

    val startedAt: Long

    fun newId(): Long

    fun newSequence(parent : AdaptiveFragment, index : Int) : AdaptiveFragment =
        AdaptiveSequence(parent.adapter, parent, index)

    fun newSelect(parent : AdaptiveFragment, index : Int) : AdaptiveFragment =
        AdaptiveSelect(parent.adapter, parent, index)

    fun newLoop(parent : AdaptiveFragment, index : Int) : AdaptiveFragment =
        AdaptiveLoop<Any>(parent.adapter, parent, index)

    fun actualize(name: String, parent: AdaptiveFragment, index: Int) =
        fragmentFactory.newInstance(name, parent, index)

    fun addActualRoot(fragment: AdaptiveFragment) = Unit

    fun removeActualRoot(fragment: AdaptiveFragment) = Unit

    /**
     * Called by the `adaptive` entry point function after the root fragment is mounted.
     */
    fun mounted() = Unit

    fun trace(fragment: AdaptiveFragment, point: String, data: String) {
        if (fragment.trace && fragment.tracePatterns.any { it.matches(point) }) {
            TraceEvent(fragment.name(), fragment.id, point, data).println(startedAt)
        }
    }

    fun trace(point: String, data: String) {
        if (trace.any { it.matches(point) }) {
            TraceEvent("<adapter>", - 1, point, data).println(startedAt)
        }
    }

    fun log(fragment: AdaptiveFragment, point: String, data: String) {
        TraceEvent(fragment.name(), fragment.id, point, data).println(startedAt)
    }

    fun log(point: String, data: String) {
        TraceEvent("<adapter>", -1, point, data).println(startedAt)
    }

    fun AdaptiveFragment.name() =
        instructions.firstOrNullIfInstance<Name>()?.name
            ?: this::class.simpleName
            ?: "<unknown>"
}