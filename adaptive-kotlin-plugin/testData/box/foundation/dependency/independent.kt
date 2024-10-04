/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun IndependentVariable(i : Int) {

    val di = i + 3

    @Independent
    val ii = i + 2

    T1(di + ii)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        var v1 = 10
        IndependentVariable(v1)
    }.apply {
        rootFragment.setStateVariable(0, 20)
        rootFragment.patchInternal()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [10]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "before-Create", ""),
        TraceEvent("AdaptiveIndependentVariable", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [10, null, null]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [10, null, null]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [10, 13, 12]"),
        TraceEvent("AdaptiveT1", 4, "before-Create", ""),
        TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [25]"),
        TraceEvent("AdaptiveT1", 4, "after-Create", ""),
        TraceEvent("AdaptiveIndependentVariable", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveIndependentVariable", 3, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
        TraceEvent("AdaptiveIndependentVariable", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [20]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [10, 13, 12]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [20, 13, 12]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [20, 13, 12]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000000 state: [25]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000001 state: [35]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000001 state: [35]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [35]"),
        TraceEvent("AdaptiveIndependentVariable", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [20, 23, 12]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [20]")
    ))
}