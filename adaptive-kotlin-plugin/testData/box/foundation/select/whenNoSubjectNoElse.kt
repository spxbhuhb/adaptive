/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun WhenNoSubjectNoElse(item : String) {
    when {
        item == "a" -> T1(12)
        item == "b" -> T1(23)
    }
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val v1 = "a"
        WhenNoSubjectNoElse(v1)
    }.apply {
        rootFragment.setStateVariable(1, "b")
        rootFragment.patchInternalBatch()
        rootFragment.setStateVariable(1, "c")
        rootFragment.patchInternalBatch()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Create", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, a]"),
        TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 1, null]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 1, null]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 1, 1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Mount", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, b]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, b]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, b]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 1, 1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x0000000a state: [null, 2, 1]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x0000000a state: [null, 2, 1]"),
        TraceEvent("AdaptiveT1", 5, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 5, "after-Unmount", ""),
        TraceEvent("AdaptiveT1", 5, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 5, "after-Dispose", ""),
        TraceEvent("AdaptiveT1", 6, "before-Create", ""),
        TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0x0000000a thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0x0000000a thisMask: 0xffffffff state: [null, 23]"),
        TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0x0000000a thisMask: 0xffffffff state: [null, 23]"),
        TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0x0000000a thisMask: 0x00000000 state: [null, 23]"),
        TraceEvent("AdaptiveT1", 6, "after-Create", ""),
        TraceEvent("AdaptiveT1", 6, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 6, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 2, 2]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, b]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, b]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, c]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, b]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, c]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, c]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 2, 2]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x0000000a state: [null, -1, 2]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x0000000a state: [null, -1, 2]"),
        TraceEvent("AdaptiveT1", 6, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 6, "after-Unmount", ""),
        TraceEvent("AdaptiveT1", 6, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 6, "after-Dispose", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, -1, -1]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, c]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, c]")
    ))
}