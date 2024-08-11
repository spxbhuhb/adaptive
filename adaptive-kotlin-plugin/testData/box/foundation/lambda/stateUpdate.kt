/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        var a = 12
        S1 { a = a + it + 1 }
    }

    val s1 = adapter.rootFragment.children.first() as AdaptiveS1

    s1.s0(45)
    s1.createClosure.owner.patchInternal()

    return adapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveS1", 3, "before-Create", ""),
            TraceEvent("AdaptiveS1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [Function]"),
            TraceEvent("AdaptiveS1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [Function]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [Function]"),
            TraceEvent("AdaptiveS1", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveS1", 3, "before-Mount", ""),
            TraceEvent("AdaptiveS1", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [58]"),
            TraceEvent("AdaptiveS1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [Function]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [Function]"),
            TraceEvent("AdaptiveS1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [Function]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [Function]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [58]")
        )
    )
}