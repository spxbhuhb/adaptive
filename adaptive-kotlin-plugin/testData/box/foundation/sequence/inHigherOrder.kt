/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun inHigherOrder(i: Int) {
    inner {
        T1(i)
        T1(i + 1)
    }
}

@Adaptive
fun inner(@Adaptive builder: () -> Unit) {
    builder()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val i = 12
        inHigherOrder(i)
    }.apply {
        rootFragment.setStateVariable(0, 23)
        rootFragment.patchInternal()
    }

    return adapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "before-Create", ""),
            TraceEvent("AdaptiveInHigherOrder", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveInner", 4, "before-Create", ""),
            TraceEvent("AdaptiveInner", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveInner", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveInner", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveInner", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveSequence", 6, "before-Create", ""),
            TraceEvent("AdaptiveSequence", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveSequence", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [2, 3]"),
            TraceEvent("AdaptiveSequence", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [2, 3]"),
            TraceEvent("AdaptiveSequence", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [2, 3]"),
            TraceEvent("AdaptiveT1", 7, "before-Create", ""),
            TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 7, "after-Create", ""),
            TraceEvent("AdaptiveT1", 8, "before-Create", ""),
            TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
            TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptiveT1", 8, "after-Create", ""),
            TraceEvent("AdaptiveSequence", 6, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 5, "after-Create", ""),
            TraceEvent("AdaptiveInner", 4, "after-Create", ""),
            TraceEvent("AdaptiveInHigherOrder", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveInHigherOrder", 3, "before-Mount", ""),
            TraceEvent("AdaptiveInner", 4, "before-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 5, "before-Mount", ""),
            TraceEvent("AdaptiveSequence", 6, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 7, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 7, "after-Mount", ""),
            TraceEvent("AdaptiveT1", 8, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 8, "after-Mount", ""),
            TraceEvent("AdaptiveSequence", 6, "after-Mount", ""),
            TraceEvent("AdaptiveAnonymous", 5, "after-Mount", ""),
            TraceEvent("AdaptiveInner", 4, "after-Mount", ""),
            TraceEvent("AdaptiveInHigherOrder", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
            TraceEvent("AdaptiveInner", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveInner", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveInner", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: []"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: []"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: []"),
            TraceEvent("AdaptiveSequence", 6, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 3]"),
            TraceEvent("AdaptiveSequence", 6, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 3]"),
            TraceEvent("AdaptiveSequence", 6, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 3]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000001 state: [23]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000001 state: [23]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [23]"),
            TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000001 state: [24]"),
            TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000001 state: [24]"),
            TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [24]"),
            TraceEvent("AdaptiveSequence", 6, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 3]"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: []"),
            TraceEvent("AdaptiveInner", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [BoundFragmentFactory(3,1)]"),
            TraceEvent("AdaptiveInHigherOrder", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]")
            //@formatter:on
        )
    )
}