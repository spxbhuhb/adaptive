/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun OnlyExternal(i: Int, s: String) {
    T1(i)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        OnlyExternal(123, "abc")
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveOnlyExternal", 3, "before-Create", ""),
        TraceEvent("AdaptiveOnlyExternal", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveOnlyExternal", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 123, abc]"),
        TraceEvent("AdaptiveOnlyExternal", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 123, abc]"),
        TraceEvent("AdaptiveOnlyExternal", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 123, abc]"),
        TraceEvent("AdaptiveT1", 4, "before-Create", ""),
        TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 123]"),
        TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 123]"),
        TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 123]"),
        TraceEvent("AdaptiveT1", 4, "after-Create", ""),
        TraceEvent("AdaptiveOnlyExternal", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveOnlyExternal", 3, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
        TraceEvent("AdaptiveOnlyExternal", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}