/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.internal

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey

class NamedFragmentFactory(
    val key: FragmentKey,
    val buildFun : (parent : AdaptiveFragment, index : Int, stateSize : Int) -> AdaptiveFragment
) {
    fun build(parent: AdaptiveFragment, index: Int, stateSize : Int = 0) : AdaptiveFragment {
        return buildFun(parent, index, stateSize)
    }
}