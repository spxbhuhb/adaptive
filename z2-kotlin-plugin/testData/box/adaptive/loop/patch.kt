/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.loopTest(i: Int) {
    T1(i + 3)
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        val i = 100
        for (j in listOf(i)) {
            loopTest(j)
        }
    }.apply {
        rootFragment.setStateVariable(0, 200)
        rootFragment.patchInternal()
    }

    return AdaptiveTestAdapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [100]"),
            TraceEvent("AdaptiveLoop", 3, "before-Create", ""),
            TraceEvent("AdaptiveLoop", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
            TraceEvent("AdaptiveLoop", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [<iterator>,AdaptiveFragmentFactory(2,1)]"),
            TraceEvent("AdaptiveLoop", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [<iterator>,AdaptiveFragmentFactory(2,1)]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Create", ""),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [100]"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [100]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [100]"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [100]"),
            TraceEvent("AdaptiveLoopTest", 6, "before-Create", ""),
            TraceEvent("AdaptiveLoopTest", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveLoopTest", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [100]"),
            TraceEvent("AdaptiveLoopTest", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [100]"),
            TraceEvent("AdaptiveLoopTest", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [100]"),
            TraceEvent("AdaptiveT1", 7, "before-Create", ""),
            TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [103]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [103]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [103]"),
            TraceEvent("AdaptiveT1", 7, "after-Create", ""),
            TraceEvent("AdaptiveLoopTest", 6, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 5, "after-Create", ""),
            TraceEvent("AdaptiveAnonymous", 5, "before-Mount", "bridge: 4"),
            TraceEvent("AdaptiveLoopTest", 6, "before-Mount", "bridge: 4"),
            TraceEvent("AdaptiveT1", 7, "before-Mount", "bridge: 4"),
            TraceEvent("AdaptiveT1", 7, "after-Mount", "bridge: 4"),
            TraceEvent("AdaptiveLoopTest", 6, "after-Mount", "bridge: 4"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Mount", "bridge: 4"),
            TraceEvent("AdaptiveLoop", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [<iterator>,AdaptiveFragmentFactory(2,1)]"),
            TraceEvent("AdaptiveLoop", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveLoop", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Mount", "bridge: 4"),
            TraceEvent("AdaptiveLoopTest", 6, "before-Mount", "bridge: 4"),
            TraceEvent("AdaptiveT1", 7, "before-Mount", "bridge: 4"),
            TraceEvent("AdaptiveT1", 7, "after-Mount", "bridge: 4"),
            TraceEvent("AdaptiveLoopTest", 6, "after-Mount", "bridge: 4"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Mount", "bridge: 4"),
            TraceEvent("AdaptiveLoop", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [200]"),
            TraceEvent("AdaptiveLoop", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [<iterator>,AdaptiveFragmentFactory(2,1)]"),
            TraceEvent("AdaptiveLoop", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [<iterator>,AdaptiveFragmentFactory(2,1)]"),
            TraceEvent("AdaptiveLoop", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [<iterator>,AdaptiveFragmentFactory(2,1)]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000003 state: [200]"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000003 state: [200]"),
            TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000003 state: [200]"),
            TraceEvent("AdaptiveLoopTest", 6, "before-Patch-External", "createMask: 0x00000003 thisMask: 0x00000000 state: [100]"),
            TraceEvent("AdaptiveLoopTest", 6, "after-Patch-External", "createMask: 0x00000003 thisMask: 0x00000001 state: [200]"),
            TraceEvent("AdaptiveLoopTest", 6, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000001 state: [200]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [103]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [203]"),
            TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [203]"),
            TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [203]"),
            TraceEvent("AdaptiveLoopTest", 6, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [200]"),
            TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000001 state: [200]"),
            TraceEvent("AdaptiveLoop", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [<iterator>,AdaptiveFragmentFactory(2,1)]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [200]")
            //@formatter:on
        )
    )
}