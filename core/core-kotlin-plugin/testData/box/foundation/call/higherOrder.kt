/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun higherOrderTest(i : Int) {
    higherFun(i) { lowerFunI1 ->
        higherFun(lowerFunI1) { lowerFunI2 ->
            T1(lowerFunI1 + lowerFunI2)
        }
    }
}

@Adaptive
fun higherFun(higherI: Int, lowerFun: @Adaptive (lowerFunI: Int) -> Unit) {
    higherFunInner(higherI * 2) { lowerFunInnerI ->
        lowerFun(higherI + lowerFunInnerI)
    }
}

@Adaptive
fun higherFunInner(innerI: Int, lowerFunInner: @Adaptive (lowerFunInnerI: Int) -> Unit) {
    lowerFunInner(innerI + 1)
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val i = 12
        higherOrderTest(i)
    }.apply {
        rootFragment.setStateVariable(1, 120)
        rootFragment.patchInternalBatch()
    }

    return adapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Create", ""),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Create", ""),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Create", ""),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 25]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 25]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 25]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 37]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 37]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 37]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Create", ""),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Create", ""),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 75]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 75]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 75]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 112]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 112]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 112]"),
            TraceEvent("AdaptiveT1", 12, "before-Create", ""),
            TraceEvent("AdaptiveT1", 12, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 149]"),
            TraceEvent("AdaptiveT1", 12, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 149]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 149]"),
            TraceEvent("AdaptiveT1", 12, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 11, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 10, "after-Create", ""),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Create", ""),
            TraceEvent("AdaptiveHigherFun", 8, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 7, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 6, "after-Create", ""),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Create", ""),
            TraceEvent("AdaptiveHigherFun", 4, "after-Create", ""),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Mount", ""),
            TraceEvent("AdaptiveHigherFun", 4, "before-Mount", ""),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 6, "before-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 7, "before-Mount", ""),
            TraceEvent("AdaptiveHigherFun", 8, "before-Mount", ""),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 10, "before-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 11, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 12, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 12, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 11, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 10, "after-Mount", ""),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Mount", ""),
            TraceEvent("AdaptiveHigherFun", 8, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 7, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 6, "after-Mount", ""),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Mount", ""),
            TraceEvent("AdaptiveHigherFun", 4, "after-Mount", ""),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 120]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 120]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 120]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 120, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 120, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000006 state: [null, 240, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000006 state: [null, 240, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0x00000006 thisMask: 0x00000002 state: [null, 25]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0x00000006 thisMask: 0x00000012 state: [null, 241]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000012 state: [null, 241]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-External", "createMask: 0x00000012 thisMask: 0x00000002 state: [null, 37]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-External", "createMask: 0x00000012 thisMask: 0x0000000a state: [null, 361]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-Internal", "createMask: 0x00000012 thisMask: 0x0000000a state: [null, 361]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-External", "createMask: 0x0000000a thisMask: 0x00000000 state: [null, 37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-External", "createMask: 0x0000000a thisMask: 0x00000006 state: [null, 361, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-Internal", "createMask: 0x0000000a thisMask: 0x00000006 state: [null, 361, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-External", "createMask: 0x00000006 thisMask: 0x00000000 state: [null, 74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-External", "createMask: 0x00000006 thisMask: 0x00000006 state: [null, 722, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000006 state: [null, 722, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-External", "createMask: 0x00000006 thisMask: 0x00000006 state: [null, 75]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-External", "createMask: 0x00000006 thisMask: 0x00000016 state: [null, 723]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000016 state: [null, 723]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-External", "createMask: 0x00000016 thisMask: 0x0000000a state: [null, 112]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-External", "createMask: 0x00000016 thisMask: 0x0000002a state: [null, 1084]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-Internal", "createMask: 0x00000016 thisMask: 0x0000002a state: [null, 1084]"),
            TraceEvent("AdaptiveT1", 12, "before-Patch-External", "createMask: 0x0000002a thisMask: 0x00000000 state: [null, 149]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-External", "createMask: 0x0000002a thisMask: 0x00000002 state: [null, 1445]"),
            TraceEvent("AdaptiveT1", 12, "before-Patch-Internal", "createMask: 0x0000002a thisMask: 0x00000002 state: [null, 1445]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-Internal", "createMask: 0x0000002a thisMask: 0x00000000 state: [null, 1445]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-Internal", "createMask: 0x00000016 thisMask: 0x0000000a state: [null, 1084]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000006 state: [null, 723]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000000 state: [null, 722, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-Internal", "createMask: 0x0000000a thisMask: 0x00000000 state: [null, 361, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-Internal", "createMask: 0x00000012 thisMask: 0x00000002 state: [null, 361]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000002 state: [null, 241]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 240, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 120, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 120]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 120]")
            //@formatter:on
        )
    )
}