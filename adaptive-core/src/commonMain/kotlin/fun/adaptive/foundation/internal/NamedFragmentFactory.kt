/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.internal

import `fun`.adaptive.foundation.AdaptiveFragment

class NamedFragmentFactory(
    val name: String,
    val buildFun : (parent : AdaptiveFragment, index : Int) -> AdaptiveFragment
) {
    fun build(parent: AdaptiveFragment, index: Int) : AdaptiveFragment {
        return buildFun(parent, index)
    }
}