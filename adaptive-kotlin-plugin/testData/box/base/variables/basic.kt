/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun Basic() {
    T1(12)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        Basic()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveBasic", 3, "before-Create", ""),
        TraceEvent("AdaptiveBasic", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveBasic", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveBasic", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveBasic", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveT1", 4, "before-Create", ""),
        TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 4, "after-Create", ""),
        TraceEvent("AdaptiveBasic", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveBasic", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 4, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 4, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveBasic", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
    ))
}