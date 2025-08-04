/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.testing.*

class A(
    val br : @Adaptive (i : Int) -> AdaptiveFragment
) {
    override fun toString(): String {
        return "A"
    }
}

@Adaptive
fun a(p : @Adaptive (Int) -> AdaptiveFragment) {
    p(12) .. name("hello")
}

@Adaptive
fun b(i : Int) : AdaptiveFragment {
    T1(i)
    return fragment()
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val aa = A(::b)
        a(aa.br)
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
        TraceEvent("AdaptiveAnonymous", 4, "before-Create", ""),
        TraceEvent("AdaptiveAnonymous", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveAnonymous", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [[Name(name=hello)], 12]"),
        TraceEvent("AdaptiveAnonymous", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [[Name(name=hello)], 12]"),
        TraceEvent("AdaptiveAnonymous", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [[Name(name=hello)], 12]"),
        TraceEvent("AdaptiveB", 5, "before-Create", ""),
        TraceEvent("AdaptiveB", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveB", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [[Name(name=hello)], 12]"),
        TraceEvent("AdaptiveB", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [[Name(name=hello)], 12]"),
        TraceEvent("AdaptiveB", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [[Name(name=hello)], 12]"),
        TraceEvent("AdaptiveSequence", 6, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
        TraceEvent("AdaptiveSequence", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1]]"),
        TraceEvent("AdaptiveSequence", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1]]"),
        TraceEvent("AdaptiveT1", 7, "before-Create", ""),
        TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 7, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[1]]"),
        TraceEvent("AdaptiveSequence", 6, "after-Create", ""),
        TraceEvent("AdaptiveB", 5, "after-Create", ""),
        TraceEvent("AdaptiveAnonymous", 4, "after-Create", ""),
        TraceEvent("AdaptiveA", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveA", 3, "before-Mount", ""),
        TraceEvent("AdaptiveAnonymous", 4, "before-Mount", ""),
        TraceEvent("AdaptiveB", 5, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 6, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 7, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 7, "after-Mount", ""),
        TraceEvent("AdaptiveSequence", 6, "after-Mount", ""),
        TraceEvent("AdaptiveB", 5, "after-Mount", ""),
        TraceEvent("AdaptiveAnonymous", 4, "after-Mount", ""),
        TraceEvent("AdaptiveA", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}