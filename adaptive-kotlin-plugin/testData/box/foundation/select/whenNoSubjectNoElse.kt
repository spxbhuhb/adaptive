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
        rootFragment.setStateVariable(0, "b")
        rootFragment.patchInternal()
        rootFragment.setStateVariable(0, "c")
        rootFragment.patchInternal()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Create", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [a]"),
        TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, null]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, null]"),
        TraceEvent("AdaptiveT1", 6, "before-Create", ""),
        TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 6, "after-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 6, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 6, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Mount", ""),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [b]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [a]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [b]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [b]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [1, 1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
        TraceEvent("AdaptiveT1", 6, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 6, "after-Unmount", ""),
        TraceEvent("AdaptiveT1", 6, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 6, "after-Dispose", ""),
        TraceEvent("AdaptiveT1", 7, "before-Create", ""),
        TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 7, "after-Create", ""),
        TraceEvent("AdaptiveT1", 7, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 7, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [b]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [b]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [c]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [b]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [c]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [c]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [-1, 2]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [-1, 2]"),
        TraceEvent("AdaptiveT1", 7, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 7, "after-Unmount", ""),
        TraceEvent("AdaptiveT1", 7, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 7, "after-Dispose", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [-1, -1]"),
        TraceEvent("AdaptiveWhenNoSubjectNoElse", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [c]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [c]")
    ))
}