/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

class TestClass(
    val label : String,
    val value : Int
) : LifecycleBound {

    override fun patch(fragment : AdaptiveFragment, index : Int) {
        fragment.adapter.trace(fragment, "bound-patch", "$label $value")
    }

    override fun mount(fragment : AdaptiveFragment, index : Int) {
        fragment.adapter.trace(fragment, "bound-mount", "$label $value")
    }

    override fun unmount(fragment : AdaptiveFragment, index : Int) {
        fragment.adapter.trace(fragment, "bound-unmount", "$label $value")
    }

    override fun dispose(fragment : AdaptiveFragment, index : Int) {
        fragment.adapter.trace(fragment, "bound-dispose", "$label $value")
    }

    override fun toString() = "TestClass"

}

@Adaptive
fun outer(a : Int) {
    val lifecycleBound = TestClass("outer", a)
    if (a < 20) {
        inner(a + 1)
    } else {
        inner(a + 2)
    }
}

@Adaptive
fun inner(b : Int) {
    val lifecycleBound = TestClass("inner", b)
}



fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        outer(12)
    }

    val o = adapter.rootFragment.children.first()
    o.setStateVariable(1, 23)
    o.patchInternalBatch()

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveOuter", 3, "before-Create", ""),
        TraceEvent("AdaptiveOuter", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveOuter", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, null]"),
        TraceEvent("AdaptiveOuter", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, null]"),
        TraceEvent("AdaptiveOuter", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12, TestClass]"),
        TraceEvent("AdaptiveOuter", 3, "bound-patch", "outer 12"),
        TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 1, null]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 1, null]"),
        TraceEvent("AdaptiveInner", 5, "before-Create", ""),
        TraceEvent("AdaptiveInner", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveInner", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 13, null]"),
        TraceEvent("AdaptiveInner", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 13, null]"),
        TraceEvent("AdaptiveInner", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 13, TestClass]"),
        TraceEvent("AdaptiveInner", 5, "bound-patch", "inner 13"),
        TraceEvent("AdaptiveSequence", 6, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
        TraceEvent("AdaptiveSequence", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[]]"),
        TraceEvent("AdaptiveSequence", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[]]"),
        TraceEvent("AdaptiveSequence", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[]]"),
        TraceEvent("AdaptiveSequence", 6, "after-Create", ""),
        TraceEvent("AdaptiveInner", 5, "after-Create", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 1, 1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
        TraceEvent("AdaptiveOuter", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveOuter", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "before-Mount", ""),
        TraceEvent("AdaptiveInner", 5, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 6, "after-Mount", ""),
        TraceEvent("AdaptiveInner", 5, "bound-mount", "inner 13"),
        TraceEvent("AdaptiveInner", 5, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Mount", ""),
        TraceEvent("AdaptiveOuter", 3, "bound-mount", "outer 12"),
        TraceEvent("AdaptiveOuter", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("AdaptiveOuter", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000002 state: [null, 23, TestClass]"),
        TraceEvent("AdaptiveOuter", 3, "bound-dispose", "outer 12"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000006 thisMask: 0x00000006 state: [null, 1, 1]"),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000006 thisMask: 0x00000016 state: [null, 2, 1]"),
        TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000016 state: [null, 2, 1]"),
        TraceEvent("AdaptiveInner", 5, "before-Unmount", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Unmount", ""),
        TraceEvent("AdaptiveSequence", 6, "after-Unmount", ""),
        TraceEvent("AdaptiveInner", 5, "bound-unmount", "inner 13"),
        TraceEvent("AdaptiveInner", 5, "after-Unmount", ""),
        TraceEvent("AdaptiveInner", 5, "before-Dispose", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Dispose", ""),
        TraceEvent("AdaptiveSequence", 6, "after-Dispose", ""),
        TraceEvent("AdaptiveInner", 5, "bound-dispose", "inner 13"),
        TraceEvent("AdaptiveInner", 5, "after-Dispose", ""),
        TraceEvent("AdaptiveInner", 7, "before-Create", ""),
        TraceEvent("AdaptiveInner", 7, "before-Patch-External", "createMask: 0x00000016 thisMask: 0xffffffff state: [null, null, null]"),
        TraceEvent("AdaptiveInner", 7, "after-Patch-External", "createMask: 0x00000016 thisMask: 0xffffffff state: [null, 25, null]"),
        TraceEvent("AdaptiveInner", 7, "before-Patch-Internal", "createMask: 0x00000016 thisMask: 0xffffffff state: [null, 25, null]"),
        TraceEvent("AdaptiveInner", 7, "after-Patch-Internal", "createMask: 0x00000016 thisMask: 0x00000000 state: [null, 25, TestClass]"),
        TraceEvent("AdaptiveInner", 7, "bound-patch", "inner 25"),
        TraceEvent("AdaptiveSequence", 8, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
        TraceEvent("AdaptiveSequence", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[]]"),
        TraceEvent("AdaptiveSequence", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[]]"),
        TraceEvent("AdaptiveSequence", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[]]"),
        TraceEvent("AdaptiveSequence", 8, "after-Create", ""),
        TraceEvent("AdaptiveInner", 7, "after-Create", ""),
        TraceEvent("AdaptiveInner", 7, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 8, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 8, "after-Mount", ""),
        TraceEvent("AdaptiveInner", 7, "bound-mount", "inner 25"),
        TraceEvent("AdaptiveInner", 7, "after-Mount", ""),
        TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000006 thisMask: 0x00000006 state: [null, 2, 2]"),
        TraceEvent("AdaptiveOuter", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 23, TestClass]")
    ))
}