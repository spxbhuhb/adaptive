/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.internal

import hu.simplexion.adaptive.foundation.AdaptiveFragment

class NamedFragmentFactory(
    val name: String,
    val buildFun : (parent : AdaptiveFragment, index : Int) -> AdaptiveFragment
) {
    fun build(parent: AdaptiveFragment, index: Int) : AdaptiveFragment {
        return buildFun(parent, index)
    }
}