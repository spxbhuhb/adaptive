/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.SequenceTestComponent() {
    T0()
    T0()
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        SequenceTestComponent()
    }

    return AdaptiveTestAdapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Create", ""),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveSequence", 4, "before-Create", ""),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 2]"),
            TraceEvent("AdaptiveT0", 5, "before-Create", ""),
            TraceEvent("AdaptiveT0", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveT0", 5, "after-Create", ""),
            TraceEvent("AdaptiveT0", 8, "before-Create", ""),
            TraceEvent("AdaptiveT0", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveT0", 8, "after-Create", ""),
            TraceEvent("AdaptiveSequence", 4, "after-Create", ""),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveSequence", 4, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT0", 5, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT0", 5, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT0", 8, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT0", 8, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveSequence", 4, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
        )
    )
}