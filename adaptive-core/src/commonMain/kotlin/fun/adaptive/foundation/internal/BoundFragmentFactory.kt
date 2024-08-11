/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.internal

import `fun`.adaptive.foundation.AdaptiveFragment

class BoundFragmentFactory(
    val declaringFragment: AdaptiveFragment,
    val declarationIndex : Int
) {
    fun build(parent: AdaptiveFragment) : AdaptiveFragment? {
        return declaringFragment.genBuild(parent, declarationIndex)
    }

    override fun toString() =
        "BoundFragmentFactory(${declaringFragment.id},$declarationIndex)"
}