/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun higherOrderTest(i : Int) {
    higherFun(i) { lowerFunI1 ->
        higherFun(lowerFunI1) { lowerFunI2 ->
            T1(lowerFunI1 + lowerFunI2)
        }
    }
}

@Adaptive
fun higherFun(higherI: Int, @Adaptive lowerFun: (lowerFunI: Int) -> Unit) {
    higherFunInner(higherI * 2) { lowerFunInnerI ->
        lowerFun(higherI + lowerFunInnerI)
    }
}

@Adaptive
fun higherFunInner(innerI: Int, @Adaptive lowerFunInner: (lowerFunInnerI: Int) -> Unit) {
    lowerFunInner(innerI + 1)
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val i = 12
        higherOrderTest(i)
    }.apply {
        rootFragment.setStateVariable(0, 120)
        rootFragment.patchInternal()
    }

    return adapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Create", ""),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Create", ""),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Create", ""),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [25]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [37]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [37]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Create", ""),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Create", ""),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [75]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [75]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [75]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [112]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [112]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [112]"),
            TraceEvent("AdaptiveT1", 12, "before-Create", ""),
            TraceEvent("AdaptiveT1", 12, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [149]"),
            TraceEvent("AdaptiveT1", 12, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [149]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [149]"),
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
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 12, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 12, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [120]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [120]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [120]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [120, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [120, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [24, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [240, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [240, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000001 state: [25]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000005 state: [241]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000005 state: [241]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-External", "createMask: 0x00000005 thisMask: 0x00000001 state: [37]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-External", "createMask: 0x00000005 thisMask: 0x00000003 state: [361]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-Internal", "createMask: 0x00000005 thisMask: 0x00000003 state: [361]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000000 state: [37, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000003 state: [361, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFun", 8, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000003 state: [361, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000000 state: [74, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000003 state: [722, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000003 state: [722, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000003 state: [75]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000007 state: [723]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000007 state: [723]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-External", "createMask: 0x00000007 thisMask: 0x00000003 state: [112]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-External", "createMask: 0x00000007 thisMask: 0x00000007 state: [1084]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-Internal", "createMask: 0x00000007 thisMask: 0x00000007 state: [1084]"),
            TraceEvent("AdaptiveT1", 12, "before-Patch-External", "createMask: 0x00000007 thisMask: 0x00000000 state: [149]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-External", "createMask: 0x00000007 thisMask: 0x00000001 state: [1445]"),
            TraceEvent("AdaptiveT1", 12, "before-Patch-Internal", "createMask: 0x00000007 thisMask: 0x00000001 state: [1445]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-Internal", "createMask: 0x00000007 thisMask: 0x00000000 state: [1445]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-Internal", "createMask: 0x00000007 thisMask: 0x00000003 state: [1084]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000003 state: [723]"),
            TraceEvent("AdaptiveHigherFunInner", 9, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [722, BoundFragmentFactory(8,1)]"),
            TraceEvent("AdaptiveHigherFun", 8, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [361, BoundFragmentFactory(3,2)]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-Internal", "createMask: 0x00000005 thisMask: 0x00000001 state: [361]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000001 state: [241]"),
            TraceEvent("AdaptiveHigherFunInner", 5, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [240, BoundFragmentFactory(4,1)]"),
            TraceEvent("AdaptiveHigherFun", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [120, BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveHigherOrderTest", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [120]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [120]")
            //@formatter:on
        )
    )
}