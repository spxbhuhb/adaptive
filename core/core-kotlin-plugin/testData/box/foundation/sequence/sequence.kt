/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun SequenceTestComponent() {
    T0()
    T0()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        SequenceTestComponent()
    }

    return adapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Create", ""),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveSequence", 4, "before-Create", ""),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1, 2]]"),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1, 2]]"),
            TraceEvent("AdaptiveT0", 5, "before-Create", ""),
            TraceEvent("AdaptiveT0", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveT0", 5, "after-Create", ""),
            TraceEvent("AdaptiveT0", 6, "before-Create", ""),
            TraceEvent("AdaptiveT0", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveT0", 6, "after-Create", ""),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[1, 2]]"),
            TraceEvent("AdaptiveSequence", 4, "after-Create", ""),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "before-Mount", ""),
            TraceEvent("AdaptiveSequence", 4, "before-Mount", ""),
            TraceEvent("AdaptiveT0", 5, "before-Mount", ""),
            TraceEvent("AdaptiveT0", 5, "after-Mount", ""),
            TraceEvent("AdaptiveT0", 6, "before-Mount", ""),
            TraceEvent("AdaptiveT0", 6, "after-Mount", ""),
            TraceEvent("AdaptiveSequence", 4, "after-Mount", ""),
            TraceEvent("AdaptiveSequenceTestComponent", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", "")
        )
    )
}