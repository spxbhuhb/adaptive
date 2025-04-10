/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service

import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

fun hello(b : () -> Int) : Int {
    return b()
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val i = hello { 12 }
        T1(i)
    }

    return return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 3, "before-Create", ""),
        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 3, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}