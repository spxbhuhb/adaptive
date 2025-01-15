/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

@AdaptiveExpect("c")
fun test() {
    manualImplementation()
}

@AdaptiveExpect(commonUI)
fun otherTest() {
    manualImplementation()
}

const val commonUI = "c"

@AdaptiveActual("c")
open class AdaptiveTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, -1, 0) {

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal() = true

}

@AdaptiveActual(commonUI)
class AdaptiveOtherTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AdaptiveTest(adapter, parent, declarationIndex)


fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adapter.fragmentFactory.add("c:test") { p,i,s -> AdaptiveTest(p.adapter as AdaptiveTestAdapter, p, i) }
    adapter.fragmentFactory.add("c:othertest") { p,i,s -> AdaptiveTest(p.adapter as AdaptiveTestAdapter, p, i) }

    adaptive(adapter) {
        test()
        otherTest()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
        TraceEvent("AdaptiveTest", 4, "before-Create", ""),
        TraceEvent("AdaptiveTest", 4, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTest", 4, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTest", 4, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTest", 4, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveTest", 4, "after-Create", ""),
        TraceEvent("AdaptiveTest", 5, "before-Create", ""),
        TraceEvent("AdaptiveTest", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTest", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTest", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTest", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveTest", 5, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 2]"),
        TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Mount", ""),
        TraceEvent("AdaptiveTest", 4, "before-Mount", ""),
        TraceEvent("AdaptiveTest", 4, "after-Mount", ""),
        TraceEvent("AdaptiveTest", 5, "before-Mount", ""),
        TraceEvent("AdaptiveTest", 5, "after-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}