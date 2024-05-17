/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun IfOnlyTrue(i : Int) {
    if (i % 2 == 0) {
        T0()
    }
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        IfOnlyTrue(6)
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Create", ""),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [6]"),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [6]"),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [6]"),
        TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, -1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, -1]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, -1]"),
        TraceEvent("AdaptiveT0", 6, "before-Create", ""),
        TraceEvent("AdaptiveT0", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveT0", 6, "after-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT0", 6, "before-Mount", ""),
        TraceEvent("AdaptiveT0", 6, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Mount", ""),
        TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}