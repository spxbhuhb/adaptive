/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun t(f : () -> Unit) {

}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        var a = 12
        t { a = 13 }
    }

    val r = adapter.rootFragment
    val t = r.children.first()
    @Suppress("UNCHECKED_CAST")
    val f = t.state[1] as (() -> Unit)
    f.invoke()

    if (r.state[1] != 13) return "Fail: r.state[1] = ${r.state[1]}"

    return adapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveT", 3, "before-Create", ""),
            TraceEvent("AdaptiveT", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, Function]"),
            TraceEvent("AdaptiveT", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, Function]"),
            TraceEvent("AdaptiveT", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("AdaptiveSequence", 4, "before-Create", ""),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[]]"),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[]]"),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[]]"),
            TraceEvent("AdaptiveSequence", 4, "after-Create", ""),
            TraceEvent("AdaptiveT", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveT", 3, "before-Mount", ""),
            TraceEvent("AdaptiveSequence", 4, "before-Mount", ""),
            TraceEvent("AdaptiveSequence", 4, "after-Mount", ""),
            TraceEvent("AdaptiveT", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 13]"),
            TraceEvent("AdaptiveT", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("AdaptiveT", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("AdaptiveT", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[]]"),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[]]"),
            TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[]]"),
            TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[]]"),
            TraceEvent("AdaptiveT", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, Function]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 13]")
        )
    )
}