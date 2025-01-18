/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

@AdaptiveExpect("c")
fun test(i : Int) {
    manualImplementation()
}

@AdaptiveActual("c")
open class AdaptiveTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, -1, 2) {

    val p1 by stateVariable<Int>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal() = true

}


fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adapter.fragmentFactory.add("c:test") { p,i,s -> AdaptiveTest(p.adapter as AdaptiveTestAdapter, p, i) }

    adaptive(adapter) {
        test(12)
    }
    
    return adapter.assert(listOf(

    ))
}