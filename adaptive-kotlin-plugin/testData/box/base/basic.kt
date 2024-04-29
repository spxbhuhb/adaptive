/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapterRegistry
import hu.simplexion.adaptive.base.testing.*

fun Adaptive.Basic() {
    T0()
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        Basic()
    }

    return AdaptiveTestAdapter.assert(listOf(
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
        TraceEvent("AdaptiveT0", 4, "before-Create", ""),
        TraceEvent("AdaptiveT0", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveT0", 4, "after-Create", ""),
        TraceEvent("AdaptiveBasic", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveBasic", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT0", 4, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT0", 4, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveBasic", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
    ))
}