/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

data class Test(val i : Int) : AdaptiveInstruction

val String.test
    inline get() = this.toInt()

val Number.test
    inline get() = this.toInt()

val Number.testClass
    inline get() = Test(this.toInt())

@Adaptive
fun tc(vararg instructions : AdaptiveInstruction) {
    T1(34)
    // T1(instructions.firstOrNullIfInstance<Test>()?.i ?: 999)
}

@Adaptive
fun Basic() {
    T1("12".test)
    T1(23f.test)
    tc(34.testClass)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        Basic()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveBasic", 3, "before-Create", ""),
        TraceEvent("AdaptiveBasic", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveBasic", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveBasic", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveBasic", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 4, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2, 3]"),
        TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2, 3]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptiveT1", 6, "before-Create", ""),
        TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 6, "after-Create", ""),
        TraceEvent("AdaptiveTc", 7, "before-Create", ""),
        TraceEvent("AdaptiveTc", 7, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveTc", 7, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [[Test(i=34)]]"),
        TraceEvent("AdaptiveTc", 7, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [[Test(i=34)]]"),
        TraceEvent("AdaptiveTc", 7, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [[Test(i=34)]]"),
        TraceEvent("AdaptiveT1", 8, "before-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [34]"),
        TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [34]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [34]"),
        TraceEvent("AdaptiveT1", 8, "after-Create", ""),
        TraceEvent("AdaptiveTc", 7, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 2, 3]"),
        TraceEvent("AdaptiveSequence", 4, "after-Create", ""),
        TraceEvent("AdaptiveBasic", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveBasic", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "after-Mount", ""),
        TraceEvent("AdaptiveT1", 6, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 6, "after-Mount", ""),
        TraceEvent("AdaptiveTc", 7, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "after-Mount", ""),
        TraceEvent("AdaptiveTc", 7, "after-Mount", ""),
        TraceEvent("AdaptiveSequence", 4, "after-Mount", ""),
        TraceEvent("AdaptiveBasic", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))
}