/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapterRegistry
import hu.simplexion.adaptive.base.testing.*

fun Adaptive.ManyVariables(
    i0: Int,
    i1: Int,
    i2: Int,
    i3: Int,
    i4: Int,
    i5: Int,
    i6: Int,
    i7: Int,
    i8: Int,
    i9: Int,
    i10: Int,
    i11: Int,
    i12: Int,
    i13: Int,
    i14: Int,
    i15: Int,
    i16: Int,
    i17: Int,
    i18: Int,
    i19: Int,
    i20: Int,
    i21: Int,
    i22: Int,
    i23: Int,
    i24: Int,
    i25: Int,
    i26: Int,
    i27: Int,
    i28: Int,
    i29: Int
) {
    T1(i12)
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        ManyVariables(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29
        )
    }

    return AdaptiveTestAdapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveManyVariables", 3, "before-Create", ""),
            TraceEvent("AdaptiveManyVariables", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]"),
            TraceEvent("AdaptiveManyVariables", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]"),
            TraceEvent("AdaptiveManyVariables", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]"),
            TraceEvent("AdaptiveManyVariables", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]"),
            TraceEvent("AdaptiveT1", 4, "before-Create", ""),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Create", ""),
            TraceEvent("AdaptiveManyVariables", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveManyVariables", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 4, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 4, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveManyVariables", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
        )
    )
}