/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.internal

import `fun`.adaptive.foundation.AdaptiveFragment

class BoundFragmentFactory(
    val declaringFragment: AdaptiveFragment,
    val declarationIndex: Int,
    val functionReference: ((parent: AdaptiveFragment, declarationIndex: Int) -> AdaptiveFragment?)?
) {
    override fun toString() =
        "BoundFragmentFactory(${declaringFragment.id},$declarationIndex)"
}