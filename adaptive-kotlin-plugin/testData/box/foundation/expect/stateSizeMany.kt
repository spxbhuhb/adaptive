/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

@AdaptiveExpect("c")
fun test(p1 : Int, p2 : String, p3 : Set<Int>) {
    manualImplementation()
}

@AdaptiveActual("c")
class AdaptiveTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, stateSize()) {

    val p1 : Int
        by stateVariable()

    val p2 : String
        by stateVariable()

    val p3 : Set<Int>
        by stateVariable()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal() = true

}


fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adapter.fragmentFactory.add("c:test") { p,i,s -> AdaptiveTest(p.adapter as AdaptiveTestAdapter, p, i) }

    adaptive(adapter) {
        test(12, "a", emptySet())
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveTest", 3, "before-Create", ""),
        TraceEvent("AdaptiveTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null, null]"),
        TraceEvent("AdaptiveTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, a, []]"),
        TraceEvent("AdaptiveTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, a, []]"),
        TraceEvent("AdaptiveTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12, a, []]"),
        TraceEvent("AdaptiveTest", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveTest", 3, "before-Mount", ""),
        TraceEvent("AdaptiveTest", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}