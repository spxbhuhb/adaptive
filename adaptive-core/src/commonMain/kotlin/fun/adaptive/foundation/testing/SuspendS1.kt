/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName", "unused")

package `fun`.adaptive.foundation.testing

import `fun`.adaptive.foundation.*
import `fun`.adaptive.utility.checkIfInstance

@AdaptiveExpect("test")
fun SuspendS1(supportFun : suspend (i : Int) -> Unit) {
    manualImplementation(supportFun)
}

@AdaptiveActual("test")
class AdaptiveSuspendS1(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 2) {

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = true

    var s0: (suspend (Int) -> Unit)
        get() = get(1)
        set(v) {
            set(1, v)
        }

}