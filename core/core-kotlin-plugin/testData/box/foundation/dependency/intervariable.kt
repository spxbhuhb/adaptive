/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun InterVariableDependency(i : Int) {

    val internal = i + 3
    val dependent = internal + 2

    T1(internal + dependent)

}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        var v1 = 10
        InterVariableDependency(v1)
    }.apply {
        rootFragment.setStateVariable(1, 20)
        rootFragment.patchInternalBatch()
    }

    return adapter.assert(listOf(
        //@formatter:off
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 10]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "before-Create", ""),
        TraceEvent("AdaptiveInterVariableDependency", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null, null]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 10, null, null]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 10, null, null]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 10, 13, 15]"),
        TraceEvent("AdaptiveT1", 4, "before-Create", ""),
        TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 28]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 28]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 28]"),
        TraceEvent("AdaptiveT1", 4, "after-Create", ""),
        TraceEvent("AdaptiveInterVariableDependency", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveInterVariableDependency", 3, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
        TraceEvent("AdaptiveInterVariableDependency", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 20]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 10, 13, 15]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 20, 13, 15]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 20, 13, 15]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x0000000e thisMask: 0x00000000 state: [null, 28]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x0000000e thisMask: 0x00000002 state: [null, 48]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x0000000e thisMask: 0x00000002 state: [null, 48]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x0000000e thisMask: 0x00000000 state: [null, 48]"),
        TraceEvent("AdaptiveInterVariableDependency", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 20, 23, 25]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 20]")
        //@formatter:on
    ))
}