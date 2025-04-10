/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun IfOnlyFalse(i : Int) {
    if (i % 2 == 0) {
        T0()
    }
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        IfOnlyFalse(5)
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "before-Create", ""),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 5]"),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 5]"),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 5]"),
        TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, -1, null]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, -1, null]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, -1, null]"),
        TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Mount", ""),
        TraceEvent("AdaptiveIfOnlyFalse", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}