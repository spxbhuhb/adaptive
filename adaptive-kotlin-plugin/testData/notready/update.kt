/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.adat.*
import `fun`.adaptive.adat.api.*
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.fragment.*
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.producer.*
import `fun`.adaptive.foundation.query.*
import `fun`.adaptive.foundation.testing.*

@Adat
class T(
    val ti: Int
)

@Adaptive
fun updateTest(t : T, vararg instructions: AdaptiveInstruction) {
    T1(t.ti)
}

class OnClick(val handler: () -> Unit) : AdaptiveInstruction {

    override fun execute() {
        handler()
    }

    override fun toString(): String {
        return "OnClick"
    }
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val t = copyStore { T(12) }
        updateTest(t, OnClick { t.ti.update { 23 } })
    }

    val i = adapter.firstWith<OnClick>().firstInstruction<OnClick>()
    i.execute()

    return adapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Add-Producer", "producer: CopyStore(AdaptiveStateVariableBinding(2, 0, 0, 2, 0, null, fun.adaptive.kotlin.base.success.T, fun.adaptive.kotlin.base.success.T))"),
            TraceEvent("<root>", 2, "after-Add-Producer", "producer: CopyStore(AdaptiveStateVariableBinding(2, 0, 0, 2, 0, null, fun.adaptive.kotlin.base.success.T, fun.adaptive.kotlin.base.success.T))"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [T(ti=12)]"),
            TraceEvent("AdaptiveUpdateTest", 3, "before-Create", ""),
            TraceEvent("AdaptiveUpdateTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveUpdateTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [T(ti=12), [OnClick]]"),
            TraceEvent("AdaptiveUpdateTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [T(ti=12), [OnClick]]"),
            TraceEvent("AdaptiveUpdateTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [T(ti=12), [OnClick]]"),
            TraceEvent("AdaptiveT1", 4, "before-Create", ""),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Create", ""),
            TraceEvent("AdaptiveUpdateTest", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveUpdateTest", 3, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
            TraceEvent("AdaptiveUpdateTest", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [T(ti=12)]"),
            TraceEvent("AdaptiveUpdateTest", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [T(ti=12), [OnClick]]"),
            TraceEvent("AdaptiveUpdateTest", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [T(ti=23), [OnClick]]"),
            TraceEvent("AdaptiveUpdateTest", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [T(ti=23), [OnClick]]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
            TraceEvent("AdaptiveUpdateTest", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [T(ti=23), [OnClick]]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [T(ti=23)]")
            //@formatter:on
        )
    )
}