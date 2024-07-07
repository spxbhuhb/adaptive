/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.navigation

import hu.simplexion.adaptive.foundation.AdaptiveFragment

/**
 * Each fragment may have one (and only one) [NavNode] in [AbstractNavSupport.nodes].
 * The first time the fragment uses a nav producer, a new node is created for that
 * fragment.
 *
 * Producers cannot be used indirectly (`val a = if (something) navSegment() else "b"` is
 * not allowed), so it is guaranteed that the consumed segment count is the same for all states.
 *
 * @param  owner     The fragment this nav node belongs to.
 * @param  navData   Calculated from the [navData] of the first parent fragment that has
 *                   a [NavNode] in [AbstractNavSupport.nodes].
 * @param  consumed  The number of navigation segments this nav node consumed. A segment
 *                   is consumed each time a nav producer is used.
 */
data class NavNode(
    val owner: AdaptiveFragment,
    val navData: NavData,
    val consumed: Int
) {

    fun consumeNext() = NavNode(owner, navData, consumed + 1)

    fun last() = navData.segments[consumed - 1]

}