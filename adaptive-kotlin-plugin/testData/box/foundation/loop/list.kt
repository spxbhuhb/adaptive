/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun loopTest(entries: List<Int>) {
    for (i in entries) {
        T1(i + 10)
    }
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        loopTest(listOf(0, 1, 2, 3))
    }

    return adapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveLoopTest", 3, "before-Create", ""),
            TraceEvent("AdaptiveLoopTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveLoopTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [[0, 1, 2, 3]]"),
            TraceEvent("AdaptiveLoopTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [[0, 1, 2, 3]]"),
            TraceEvent("AdaptiveLoopTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [[0, 1, 2, 3]]"),
            TraceEvent("AdaptiveLoop", 4, "before-Create", ""),
            TraceEvent("AdaptiveLoop", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
            TraceEvent("AdaptiveLoop", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [ArrayItr,BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveLoop", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [ArrayItr,BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
            TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
            TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [0]"),
            TraceEvent("AdaptiveT1", 7, "before-Create", ""),
            TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [10]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [10]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [10]"),
            TraceEvent("AdaptiveT1", 7, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 6, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 6, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 7, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 7, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 6, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 10, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
            TraceEvent("AdaptiveAnonymous", 10, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
            TraceEvent("AdaptiveAnonymous", 10, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [1]"),
            TraceEvent("AdaptiveT1", 11, "before-Create", ""),
            TraceEvent("AdaptiveT1", 11, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 11, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [11]"),
            TraceEvent("AdaptiveT1", 11, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [11]"),
            TraceEvent("AdaptiveT1", 11, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [11]"),
            TraceEvent("AdaptiveT1", 11, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 10, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 10, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 11, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 11, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 10, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 14, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 14, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
            TraceEvent("AdaptiveAnonymous", 14, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
            TraceEvent("AdaptiveAnonymous", 14, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
            TraceEvent("AdaptiveAnonymous", 14, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [2]"),
            TraceEvent("AdaptiveT1", 15, "before-Create", ""),
            TraceEvent("AdaptiveT1", 15, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 15, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 15, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 15, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 15, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 14, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 14, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 15, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 15, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 14, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 18, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 18, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
            TraceEvent("AdaptiveAnonymous", 18, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
            TraceEvent("AdaptiveAnonymous", 18, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
            TraceEvent("AdaptiveAnonymous", 18, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [3]"),
            TraceEvent("AdaptiveT1", 19, "before-Create", ""),
            TraceEvent("AdaptiveT1", 19, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 19, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
            TraceEvent("AdaptiveT1", 19, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
            TraceEvent("AdaptiveT1", 19, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptiveT1", 19, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 18, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 18, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 19, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 19, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 18, "after-Mount", ""),
            TraceEvent("AdaptiveLoop", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [ArrayItr,BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveLoop", 4, "after-Create", ""),
            TraceEvent("AdaptiveLoopTest", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveLoopTest", 3, "before-Mount", ""),
            TraceEvent("AdaptiveLoop", 4, "before-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 6, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 7, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 7, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 6, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 10, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 11, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 11, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 10, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 14, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 15, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 15, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 14, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 18, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 19, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 19, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 18, "after-Mount", ""),
            TraceEvent("AdaptiveLoop", 4, "after-Mount", ""),
            TraceEvent("AdaptiveLoopTest", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", "")
        )
    )
}