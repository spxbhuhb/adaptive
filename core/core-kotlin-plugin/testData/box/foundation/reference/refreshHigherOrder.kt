/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

class A(

    val fr : @Adaptive (i : Int) -> Unit = ::b
) {
    override fun toString(): String {
        return "A"
    }
}

@Adaptive
fun a(p : @Adaptive (Int) -> Unit) {
    inner {
        p(12)
    }
}

@Adaptive
fun inner(ip : @Adaptive () -> Unit) {
    ip()
}

@Adaptive
fun b(i : Int) {
    T1(i)
}

@Adaptive
fun c(i : Int) {
    T1(i)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val ab = A()
        a(ab.fr)
    }.apply {
        rootFragment.setStateVariable(1, A(::c))
        rootFragment.patchInternalBatch()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, A]"),
        TraceEvent("AdaptiveA", 3, "before-Create", ""),
        TraceEvent("AdaptiveA", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveA", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, BoundFragmentFactory(2,-1)]"),
        TraceEvent("AdaptiveA", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, BoundFragmentFactory(2,-1)]"),
        TraceEvent("AdaptiveA", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, BoundFragmentFactory(2,-1)]"),
        TraceEvent("AdaptiveInner", 4, "before-Create", ""),
        TraceEvent("AdaptiveInner", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveInner", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, BoundFragmentFactory(3,1)]"),
        TraceEvent("AdaptiveInner", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, BoundFragmentFactory(3,1)]"),
        TraceEvent("AdaptiveInner", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, BoundFragmentFactory(3,1)]"),
        TraceEvent("AdaptiveAnonymous", 5, "before-Create", ""),
        TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveAnonymous", 6, "before-Create", ""),
        TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveB", 7, "before-Create", ""),
        TraceEvent("AdaptiveB", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveB", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveB", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveB", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 8, "before-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 8, "after-Create", ""),
        TraceEvent("AdaptiveB", 7, "after-Create", ""),
        TraceEvent("AdaptiveAnonymous", 6, "after-Create", ""),
        TraceEvent("AdaptiveAnonymous", 5, "after-Create", ""),
        TraceEvent("AdaptiveInner", 4, "after-Create", ""),
        TraceEvent("AdaptiveA", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveA", 3, "before-Mount", ""),
        TraceEvent("AdaptiveInner", 4, "before-Mount", ""),
        TraceEvent("AdaptiveAnonymous", 5, "before-Mount", ""),
        TraceEvent("AdaptiveAnonymous", 6, "before-Mount", ""),
        TraceEvent("AdaptiveB", 7, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "after-Mount", ""),
        TraceEvent("AdaptiveB", 7, "after-Mount", ""),
        TraceEvent("AdaptiveAnonymous", 6, "after-Mount", ""),
        TraceEvent("AdaptiveAnonymous", 5, "after-Mount", ""),
        TraceEvent("AdaptiveInner", 4, "after-Mount", ""),
        TraceEvent("AdaptiveA", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, A]"),
        TraceEvent("AdaptiveA", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, BoundFragmentFactory(2,-1)]"),
        TraceEvent("AdaptiveA", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, BoundFragmentFactory(2,-1)]"),
        TraceEvent("AdaptiveA", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, BoundFragmentFactory(2,-1)]"),
        TraceEvent("AdaptiveInner", 4, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, BoundFragmentFactory(3,1)]"),
        TraceEvent("AdaptiveInner", 4, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, BoundFragmentFactory(3,1)]"),
        TraceEvent("AdaptiveInner", 4, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, BoundFragmentFactory(3,1)]"),
        TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null]"),
        TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null]"),
        TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null]"),
        TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveB", 7, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 8, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 8, "after-Unmount", ""),
        TraceEvent("AdaptiveB", 7, "after-Unmount", ""),
        TraceEvent("AdaptiveB", 7, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 8, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 8, "after-Dispose", ""),
        TraceEvent("AdaptiveB", 7, "after-Dispose", ""),
        TraceEvent("AdaptiveC", 9, "before-Create", ""),
        TraceEvent("AdaptiveC", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveC", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveC", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveC", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 10, "before-Create", ""),
        TraceEvent("AdaptiveT1", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 10, "after-Create", ""),
        TraceEvent("AdaptiveC", 9, "after-Create", ""),
        TraceEvent("AdaptiveC", 9, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 10, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 10, "after-Mount", ""),
        TraceEvent("AdaptiveC", 9, "after-Mount", ""),
        TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null]"),
        TraceEvent("AdaptiveInner", 4, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, BoundFragmentFactory(3,1)]"),
        TraceEvent("AdaptiveA", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, BoundFragmentFactory(2,-1)]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, A]")
    ))
}