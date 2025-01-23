/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*
import kotlinx.coroutines.*

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        var a = 12
        SuspendS1 { a = a + it + 1 }
    }

    val s1 = adapter.rootFragment.children.first() as AdaptiveSuspendS1

    runBlocking {
        s1.s0(45)
    }

    return adapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveSuspendS1", 3, "before-Create", ""),
            TraceEvent("AdaptiveSuspendS1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveSuspendS1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, Function]"),
            TraceEvent("AdaptiveSuspendS1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, Function]"),
            TraceEvent("AdaptiveSuspendS1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("AdaptiveSuspendS1", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveSuspendS1", 3, "before-Mount", ""),
            TraceEvent("AdaptiveSuspendS1", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 58]"),
            TraceEvent("AdaptiveSuspendS1", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("AdaptiveSuspendS1", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, Function]"),
            TraceEvent("AdaptiveSuspendS1", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, Function]"),
            TraceEvent("AdaptiveSuspendS1", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 58]")
        )
    )
}