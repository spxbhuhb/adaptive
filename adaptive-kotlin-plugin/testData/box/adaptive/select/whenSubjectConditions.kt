/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.WhenSubjectConditions(item: Number) {
    when (item) {
        in listOf(1) -> T1(12)
        is Int -> T1(23)
        else -> T1(34)
    }
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        val v1 = 1
        WhenSubjectConditions(v1)
    }.apply {
        rootFragment.setStateVariable(0, 2)
        rootFragment.patchInternal()
        rootFragment.setStateVariable(0, 3.0)
        rootFragment.patchInternal()
    }

    return AdaptiveTestAdapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Create", ""),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1]"),
            TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, -1]"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, -1]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, -1]"),
            TraceEvent("AdaptiveT1", 6, "before-Create", ""),
            TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 6, "after-Create", ""),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 1]"),
            TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveSelect", 4, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 6, "before-Mount", "bridge: 5"),
            TraceEvent("AdaptiveT1", 6, "after-Mount", "bridge: 5"),
            TraceEvent("AdaptiveSelect", 4, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [1]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [1, 1]"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
            TraceEvent("AdaptiveT1", 6, "before-Unmount", "bridge: 5"),
            TraceEvent("AdaptiveT1", 6, "after-Unmount", "bridge: 5"),
            TraceEvent("AdaptiveT1", 6, "before-Dispose", ""),
            TraceEvent("AdaptiveT1", 6, "after-Dispose", ""),
            TraceEvent("AdaptiveT1", 7, "before-Create", ""),
            TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [23]"),
            TraceEvent("AdaptiveT1", 7, "after-Create", ""),
            TraceEvent("AdaptiveT1", 7, "before-Mount", "bridge: 5"),
            TraceEvent("AdaptiveT1", 7, "after-Mount", "bridge: 5"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [2]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [2]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [3.0]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [2]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [3.0]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [3.0]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [3, 2]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [3, 2]"),
            TraceEvent("AdaptiveT1", 7, "before-Unmount", "bridge: 5"),
            TraceEvent("AdaptiveT1", 7, "after-Unmount", "bridge: 5"),
            TraceEvent("AdaptiveT1", 7, "before-Dispose", ""),
            TraceEvent("AdaptiveT1", 7, "after-Dispose", ""),
            TraceEvent("AdaptiveT1", 8, "before-Create", ""),
            TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [34]"),
            TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [34]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [34]"),
            TraceEvent("AdaptiveT1", 8, "after-Create", ""),
            TraceEvent("AdaptiveT1", 8, "before-Mount", "bridge: 5"),
            TraceEvent("AdaptiveT1", 8, "after-Mount", "bridge: 5"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [3, 3]"),
            TraceEvent("AdaptiveWhenSubjectConditions", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [3.0]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [3.0]")
        )
    )
}