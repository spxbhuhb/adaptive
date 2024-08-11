/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun IfElsePatch(i : Int) {
    if (i % 2 == 0) {
        T1(i + 200)
        T1(i + 300)
    } else {
        T1(i + 100)
    }
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        var v1 = 1005
        IfElsePatch(v1)
    }.apply {
        rootFragment.setStateVariable(0, 1006)
        rootFragment.patchInternal()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1005]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "before-Create", ""),
        TraceEvent("AdaptiveIfElsePatch", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1005]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1005]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1005]"),
        TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [4, null]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [4, null]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1105]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1105]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [1105]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [4, 4]"),
        TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
        TraceEvent("AdaptiveIfElsePatch", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveIfElsePatch", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Mount", ""),
        TraceEvent("AdaptiveIfElsePatch", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [1006]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [1005]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [1006]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [1006]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [4, 4]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [1, 4]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [1, 4]"),
        TraceEvent("AdaptiveT1", 5, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 5, "after-Unmount", ""),
        TraceEvent("AdaptiveT1", 5, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 5, "after-Dispose", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 6, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [2, 3]"),
        TraceEvent("AdaptiveSequence", 6, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [2, 3]"),
        TraceEvent("AdaptiveT1", 7, "before-Create", ""),
        TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1206]"),
        TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1206]"),
        TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [1206]"),
        TraceEvent("AdaptiveT1", 7, "after-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1306]"),
        TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1306]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [1306]"),
        TraceEvent("AdaptiveT1", 8, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 6, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000003 state: [2, 3]"),
        TraceEvent("AdaptiveSequence", 6, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 7, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 7, "after-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "after-Mount", ""),
        TraceEvent("AdaptiveSequence", 6, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [1, 1]"),
        TraceEvent("AdaptiveIfElsePatch", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [1006]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1006]")
    ))
}