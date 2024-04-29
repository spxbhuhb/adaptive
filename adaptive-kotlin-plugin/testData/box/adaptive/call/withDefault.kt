/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.WithDefault(a : Int = 12) {
    T1(a)
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        WithDefault()
        WithDefault(23)
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 2]"),
        TraceEvent("AdaptiveWithDefault", 4, "before-Create", ""),
        TraceEvent("AdaptiveWithDefault", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveWithDefault", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveWithDefault", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveWithDefault", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptiveWithDefault", 4, "after-Create", ""),
        TraceEvent("AdaptiveWithDefault", 8, "before-Create", ""),
        TraceEvent("AdaptiveWithDefault", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveWithDefault", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveWithDefault", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveWithDefault", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 9, "before-Create", ""),
        TraceEvent("AdaptiveT1", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 9, "after-Create", ""),
        TraceEvent("AdaptiveWithDefault", 8, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveWithDefault", 4, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveWithDefault", 4, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveWithDefault", 8, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 9, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 9, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveWithDefault", 8, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
    ))
}