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
class AdaptiveTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, -1, stateSize()) {

    val p1 by stateVariable<Int>()

    var patchedValue = 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal() : Boolean {
        if (haveToPatch(p1)) {
            patchedValue = p1
        }
        return false
    }

}


fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adapter.fragmentFactory.add("c:test") { p,i,s -> AdaptiveTest(p.adapter as AdaptiveTestAdapter, p, i) }

    adaptive(adapter) {
        test(12)
    }

    val t = adapter.firstFragment as AdaptiveTest

    if (t.patchedValue != 12) return "Fail: t.patchedValue != 12"

    t.setStateVariable(0, 23)
    t.patchInternalBatch()

    if (t.patchedValue != 23) return "Fail: t.patchedValue != 23"

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveTest", 3, "before-Create", ""),
        TraceEvent("AdaptiveTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveTest", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveTest", 3, "before-Mount", ""),
        TraceEvent("AdaptiveTest", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("AdaptiveTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [23]"),
        TraceEvent("AdaptiveTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]")
    ))
}