/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.*

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    @Suppress("UNCHECKED_CAST")
    val adapter = adaptive {
        var a = 12
        S1 { a = a + it + 1 }
    } as AdaptiveAdapter<TestNode>

    val s1 = adapter.rootFragment.containedFragment as AdaptiveS1<TestNode>

    s1.s0.invoke(45)
    s1.s0.declaringFragment.patchInternal()

    return AdaptiveTestAdapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveS1", 3, "before-Create", ""),
            TraceEvent("AdaptiveS1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveSupportFunction(2, 3, 0)]"),
            TraceEvent("AdaptiveS1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveSupportFunction(2, 3, 0)]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveSupportFunction(2, 3, 0)]"),
            TraceEvent("AdaptiveS1", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveS1", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveS1", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "before-Invoke", "AdaptiveSupportFunction(2, 3, 0) arguments: [45]"),
            TraceEvent("<root>", 2, "after-Invoke", "index: 0 result: kotlin.Unit"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [58]"),
            TraceEvent("AdaptiveS1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [AdaptiveSupportFunction(2, 3, 0)]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [AdaptiveSupportFunction(2, 3, 0)]"),
            TraceEvent("AdaptiveS1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [AdaptiveSupportFunction(2, 3, 0)]"),
            TraceEvent("AdaptiveS1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [AdaptiveSupportFunction(2, 3, 0)]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [58]")
        )
    )
}