/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.testing.*

fun Adaptive.Variables(i: Int, s: String) {
    val i2 = 12

    T1(0)
    T1(i)
    T1(i2)
    T1(i + i2)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        Variables(123, "abc")
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveVariables", 3, "before-Create", ""),
        TraceEvent("AdaptiveVariables", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveVariables", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [123, abc, null]"),
        TraceEvent("AdaptiveVariables", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [123, abc, null]"),
        TraceEvent("AdaptiveVariables", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [123, abc, 12]"),
        TraceEvent("AdaptiveSequence", 4, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2, 3, 4]"),
        TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2, 3, 4]"),
        TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 2, 3, 4]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [0]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [0]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [0]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [123]"),
        TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [123]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [123]"),
        TraceEvent("AdaptiveT1", 8, "after-Create", ""),
        TraceEvent("AdaptiveT1", 11, "before-Create", ""),
        TraceEvent("AdaptiveT1", 11, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 11, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 11, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 11, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 11, "after-Create", ""),
        TraceEvent("AdaptiveT1", 14, "before-Create", ""),
        TraceEvent("AdaptiveT1", 14, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 14, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [135]"),
        TraceEvent("AdaptiveT1", 14, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [135]"),
        TraceEvent("AdaptiveT1", 14, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [135]"),
        TraceEvent("AdaptiveT1", 14, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 4, "after-Create", ""),
        TraceEvent("AdaptiveVariables", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveVariables", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 4, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 8, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 8, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 11, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 11, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 14, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 14, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 4, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveVariables", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
    ))
}