/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.transformTest() {
    transformed() transform 23
}

fun Adaptive.transformed(i : Int = 12): TestState {
    return thisState()
}

interface TestState: AdaptiveTransformInterface {
    infix fun transform(i : Int) {
        setStateVariable(0, i)
    }
}

fun box() : String {

    val testAdapter = AdaptiveTestAdapter()

    adaptive(testAdapter) {
        transformTest()
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveTransformTest", 3, "before-Create", ""),
        TraceEvent("AdaptiveTransformTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTransformTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTransformTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveTransformTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveTransformed", 4, "before-Create", ""),
        TraceEvent("AdaptiveTransformed", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveTransformed", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveTransformed", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveTransformed", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveTransformed", 4, "after-Create", ""),
        TraceEvent("AdaptiveTransformTest", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveTransformTest", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveTransformed", 4, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveTransformed", 4, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveTransformTest", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
    ))
}