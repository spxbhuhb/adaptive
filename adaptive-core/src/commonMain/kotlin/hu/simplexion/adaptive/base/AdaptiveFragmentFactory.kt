/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base

class AdaptiveFragmentFactory<BT>(
    val declaringFragment: AdaptiveFragment<BT>,
    val declarationIndex : Int
) {
    fun build(parent: AdaptiveFragment<BT>) : AdaptiveFragment<BT>? {
        return declaringFragment.genBuild(parent, declarationIndex)
    }

    override fun toString() =
        "AdaptiveFragmentFactory(${declaringFragment.id},$declarationIndex)"
}