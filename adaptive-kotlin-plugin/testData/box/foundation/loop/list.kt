/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

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
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveLoopTest", 3, "before-Create", ""),
            TraceEvent("AdaptiveLoopTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveLoopTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, [0, 1, 2, 3]]"),
            TraceEvent("AdaptiveLoopTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, [0, 1, 2, 3]]"),
            TraceEvent("AdaptiveLoopTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, [0, 1, 2, 3]]"),
            TraceEvent("AdaptiveLoop", 4, "before-Create", ""),
            TraceEvent("AdaptiveLoop", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null,null]"),
            TraceEvent("AdaptiveLoop", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,ArrayItr,BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveLoop", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,ArrayItr,BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 0]"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 0]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 0]"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 0]"),
            TraceEvent("AdaptiveT1", 6, "before-Create", ""),
            TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 10]"),
            TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 10]"),
            TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 10]"),
            TraceEvent("AdaptiveT1", 6, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 5, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 7, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 1]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 1]"),
            TraceEvent("AdaptiveAnonymous", 7, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 1]"),
            TraceEvent("AdaptiveAnonymous", 7, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 1]"),
            TraceEvent("AdaptiveT1", 8, "before-Create", ""),
            TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 11]"),
            TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 11]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 11]"),
            TraceEvent("AdaptiveT1", 8, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 7, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 9, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 9, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 2]"),
            TraceEvent("AdaptiveAnonymous", 9, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 2]"),
            TraceEvent("AdaptiveAnonymous", 9, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 2]"),
            TraceEvent("AdaptiveAnonymous", 9, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 2]"),
            TraceEvent("AdaptiveT1", 10, "before-Create", ""),
            TraceEvent("AdaptiveT1", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
            TraceEvent("AdaptiveT1", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
            TraceEvent("AdaptiveT1", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 10, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 9, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 11, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 3]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 3]"),
            TraceEvent("AdaptiveAnonymous", 11, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 3]"),
            TraceEvent("AdaptiveAnonymous", 11, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 3]"),
            TraceEvent("AdaptiveT1", 12, "before-Create", ""),
            TraceEvent("AdaptiveT1", 12, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 13]"),
            TraceEvent("AdaptiveT1", 12, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 13]"),
            TraceEvent("AdaptiveT1", 12, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 13]"),
            TraceEvent("AdaptiveT1", 12, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 11, "after-Create", ""),
            TraceEvent("AdaptiveLoop", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,ArrayItr,BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveLoop", 4, "after-Create", ""),
            TraceEvent("AdaptiveLoopTest", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveLoopTest", 3, "before-Mount", ""),
            TraceEvent("AdaptiveLoop", 4, "before-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 5, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 6, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 6, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 5, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 7, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 8, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 8, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 7, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 9, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 10, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 10, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 9, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 11, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 12, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 12, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 11, "after-Mount", ""),
            TraceEvent("AdaptiveLoop", 4, "after-Mount", ""),
            TraceEvent("AdaptiveLoopTest", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", "")
        )
    )
}