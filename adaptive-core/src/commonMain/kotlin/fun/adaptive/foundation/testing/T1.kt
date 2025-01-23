/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName", "unused")

package `fun`.adaptive.foundation.testing

import `fun`.adaptive.foundation.*
import `fun`.adaptive.utility.checkIfInstance

@AdaptiveExpect("test")
fun T1(p0: Int) {
    manualImplementation(p0)
}

@AdaptiveActual("test:T1")
class AdaptiveT1(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 2) {

    val p0: Int
        get() = get(1)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = true

}