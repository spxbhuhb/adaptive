/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

enum class TE {
    First,
    Second
}

@Adaptive
fun WhenSubjectEnum(item: TE) {
    when (item) {
        TE.First -> T1(12)
        TE.Second -> T1(23)
    }
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val v1 = TE.First
        WhenSubjectEnum(v1)
    }.apply {
        rootFragment.setStateVariable(0, TE.Second)
        rootFragment.patchInternal()
    }

    return adapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [First]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "before-Create", ""),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [First]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [First]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [First]"),
            TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, null]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, null]"),
            TraceEvent("AdaptiveT1", 5, "before-Create", ""),
            TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 5, "after-Create", ""),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 1]"),
            TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "before-Mount", ""),
            TraceEvent("AdaptiveSelect", 4, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 5, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 5, "after-Mount", ""),
            TraceEvent("AdaptiveSelect", 4, "after-Mount", ""),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [Second]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [First]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [Second]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [Second]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [1, 1]"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
            TraceEvent("AdaptiveT1", 5, "before-Unmount", ""),
            TraceEvent("AdaptiveT1", 5, "after-Unmount", ""),
            TraceEvent("AdaptiveT1", 5, "before-Dispose", ""),
            TraceEvent("AdaptiveT1", 5, "after-Dispose", ""),
            TraceEvent("AdaptiveT1", 6, "before-Create", ""),
            TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
            TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
            TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [23]"),
            TraceEvent("AdaptiveT1", 6, "after-Create", ""),
            TraceEvent("AdaptiveT1", 6, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 6, "after-Mount", ""),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
            TraceEvent("AdaptiveWhenSubjectEnum", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [Second]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [Second]")
        )
    )
}