/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*
import `fun`.adaptive.foundation.producer.*
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.testing.testStoreOrNull
import `fun`.adaptive.adat.testing.TestStore

@Adat
class T(
    val ti: Int
)

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val ti = testStoreOrNull<T> { T(12) }?.ti ?: 45

        T1(ti)
    }

    @Suppress("UNCHECKED_CAST")
    (adapter.rootFragment.producers?.first() as TestStore<T>).update(T(123))

    return adapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Add-Producer", "producer: TestStore(AdaptiveStateVariableBinding(2, 1, 1, 2, 1, null, fun.adaptive.kotlin.base.success.T, fun.adaptive.kotlin.base.success.T))"),
            TraceEvent("<root>", 2, "after-Add-Producer", "producer: TestStore(AdaptiveStateVariableBinding(2, 1, 1, 2, 1, null, fun.adaptive.kotlin.base.success.T, fun.adaptive.kotlin.base.success.T))"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 3, "before-Create", ""),
            TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
            TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 3, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 123]"),
            TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 123]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 123]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 123]")
            //@formatter:on
        )
    )
}